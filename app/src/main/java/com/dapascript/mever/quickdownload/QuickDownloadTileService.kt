package com.dapascript.mever.quickdownload

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import android.os.Handler
import android.os.Looper
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.core.net.toUri
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Path.SETTING_QUICK_DOWNLOAD
import com.dapascript.mever.core.common.util.deeplink.buildMeverDeeplink
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.screen.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuickDownloadTileService : TileService() {

    @Inject
    lateinit var subscriptionRepository: SubscriptionRepository

    private var listeningJob: Job? = null

    override fun onStartListening() {
        super.onStartListening()
        listeningJob?.cancel()
        listeningJob = CoroutineScope(Dispatchers.Main).launch {
            subscriptionRepository.isProActive.collect { isPro ->
                updateTileState(isPro)
            }
        }
    }

    override fun onStopListening() {
        listeningJob?.cancel()
        listeningJob = null
        super.onStopListening()
    }

    override fun onDestroy() {
        listeningJob?.cancel()
        listeningJob = null
        super.onDestroy()
    }

    override fun onClick() {
        super.onClick()

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

        openSettingQuickDownload()
    }

    private fun updateTileState(
        pro: Boolean = isPro()
    ) {
        val tile = qsTile ?: return
        tile.state = if (pro) Tile.STATE_INACTIVE else Tile.STATE_UNAVAILABLE
        tile.label = getString(R.string.quick_download_notif_title)
        if (SDK_INT >= Build.VERSION_CODES.Q) {
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

    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun openSettingQuickDownload() {
        val deepLink = buildMeverDeeplink(path = SETTING_QUICK_DOWNLOAD)
        val intent = Intent(
            Intent.ACTION_VIEW,
            deepLink.toUri(),
            this,
            MainActivity::class.java
        ).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP
        }
        if (SDK_INT >= UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }

    @SuppressLint("StartActivityAndCollapseDeprecated")
    private fun openApp() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP
        }
        if (SDK_INT >= UPSIDE_DOWN_CAKE) {
            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
            )
            startActivityAndCollapse(pendingIntent)
        } else {
            @Suppress("DEPRECATION")
            startActivityAndCollapse(intent)
        }
    }
}