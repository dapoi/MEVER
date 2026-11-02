package com.dapascript.mever.service

import android.app.PendingIntent
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Patterns
import android.widget.Toast
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.service.QuickDownloadService
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_QUICK_NOTIF
import com.dapascript.mever.core.data.worker.MeverWorkerHelper
import com.dapascript.mever.screen.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class QuickDownloadTileService : TileService() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface QuickDownloadTileEntryPoint {
        fun subscriptionRepository(): SubscriptionRepository
    }

    private val subscriptionRepository: SubscriptionRepository by lazy {
        EntryPointAccessors.fromApplication(
            applicationContext,
            QuickDownloadTileEntryPoint::class.java
        ).subscriptionRepository()
    }

    override fun onStartListening() {
        super.onStartListening()
        updateTileState()
    }

    override fun onClick() {
        super.onClick()
        val tile = qsTile ?: return

        if (!isPro()) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.quick_tile_pro_required),
                    Toast.LENGTH_SHORT
                ).show()
            }
            openApp()
            return
        }

        val isCurrentlyEnabled = isQuickDownloadActive()

        if (!isCurrentlyEnabled) {
            val notifPermission = getNotificationPermission()
            if (notifPermission.any { checkSelfPermission(it) != PERMISSION_GRANTED }) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.permission_request_notification),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

            QuickDownloadNotificationHelper.showQuickDownloadNotification(this)
            CoroutineScope(Dispatchers.IO).launch {
                MeverDataStore(applicationContext).savePreference(KEY_QUICK_NOTIF, true)
            }
            tile.state = Tile.STATE_ACTIVE
            tile.updateTile()

            val clipboardUrl = getClipboardUrl()
            if (clipboardUrl != null) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.quick_download_processing),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                MeverWorkerHelper.enqueueDownloadWorker(this, clipboardUrl)
                QuickDownloadNotificationHelper.showQuickDownloadNotification(
                    context = this,
                    isProcessing = true,
                    processingUrl = clipboardUrl
                )
            }
        } else {
            val clipboardUrl = getClipboardUrl()
            if (clipboardUrl != null) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.quick_download_processing),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                MeverWorkerHelper.enqueueDownloadWorker(this, clipboardUrl)
                QuickDownloadNotificationHelper.showQuickDownloadNotification(
                    context = this,
                    isProcessing = true,
                    processingUrl = clipboardUrl
                )
            } else {
                QuickDownloadNotificationHelper.cancelQuickDownloadNotification(this)
                CoroutineScope(Dispatchers.IO).launch {
                    MeverDataStore(applicationContext).savePreference(KEY_QUICK_NOTIF, false)
                }
                tile.state = Tile.STATE_INACTIVE
                tile.updateTile()
            }
        }
    }

    private fun updateTileState() {
        val tile = qsTile ?: return
        val pro = isPro()
        if (!pro && isQuickDownloadActive()) {
            QuickDownloadNotificationHelper.cancelQuickDownloadNotification(this)
            CoroutineScope(Dispatchers.IO).launch {
                MeverDataStore(applicationContext).savePreference(KEY_QUICK_NOTIF, false)
            }
        }
        val isEnabled = pro && isQuickDownloadActive()
        tile.state = when {
            !pro -> Tile.STATE_UNAVAILABLE
            isEnabled -> Tile.STATE_ACTIVE
            else -> Tile.STATE_INACTIVE
        }
        tile.label = getString(R.string.quick_download_notif_title)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tile.subtitle = if (!pro) getString(R.string.pro) else null
        }
        tile.icon = Icon.createWithResource(this, R.drawable.ic_download)
        tile.updateTile()
    }

    private fun isPro(): Boolean {
        return try {
            subscriptionRepository.isProActive.value
        } catch (_: Exception) {
            false
        }
    }

    @android.annotation.SuppressLint("StartActivityAndCollapseDeprecated")
    private fun openApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }

    private fun isQuickDownloadActive(): Boolean {
        val isPrefEnabled = try {
            runBlocking(Dispatchers.IO) {
                MeverDataStore(applicationContext).getPreference(KEY_QUICK_NOTIF, false).first()
            }
        } catch (_: Exception) {
            false
        }
        return isPrefEnabled && QuickDownloadService.isRunning
    }

    private fun getClipboardUrl(): String? {
        return try {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as? ClipboardManager
            val clipData = clipboard?.primaryClip
            val item = if (clipData != null && clipData.itemCount > 0) clipData.getItemAt(0) else null
            val text = item?.text?.toString()?.trim() ?: return null
            val matcher = Patterns.WEB_URL.matcher(text)
            if (matcher.find()) {
                var url = matcher.group() ?: return null
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://$url"
                }
                url
            } else null
        } catch (_: Exception) {
            null
        }
    }
}
