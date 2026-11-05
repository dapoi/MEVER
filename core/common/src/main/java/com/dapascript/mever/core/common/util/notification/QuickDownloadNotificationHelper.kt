package com.dapascript.mever.core.common.util.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.service.quicksettings.TileService
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.service.QuickDownloadService
import com.dapascript.mever.core.common.util.getNotificationPermission
import java.util.Collections
import java.util.LinkedHashSet

object QuickDownloadNotificationHelper {

    private const val CHANNEL_ID = "quick_download_channel_silent"
    private const val CHANNEL_NAME = "Quick Download"
    const val ACTION_QUICK_DOWNLOAD_REPLY = "com.dapascript.mever.ACTION_QUICK_DOWNLOAD_REPLY"
    const val ACTION_QUICK_DOWNLOAD_DISMISS = "com.dapascript.mever.ACTION_QUICK_DOWNLOAD_DISMISS"
    const val KEY_TEXT_REPLY = "key_quick_download_text"
    const val NOTIFICATION_ID = 23041
    const val NOTIFICATION_DISMISS_ID = 23042

    private val activeProcessingUrls = Collections.synchronizedSet(LinkedHashSet<String>())

    fun createNotificationChannel(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Channel for Quick Download input notification"
            setSound(null, null)
            enableVibration(false)
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun buildNotification(context: Context): Notification {
        createNotificationChannel(context)

        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(context.getString(R.string.paste_url))
            .build()

        val replyIntent = Intent(ACTION_QUICK_DOWNLOAD_REPLY).apply {
            setPackage(context.packageName)
        }

        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_ID,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_download,
            context.getString(R.string.quick_download_action_label),
            replyPendingIntent,
        )
            .addRemoteInput(remoteInput)
            .build()

        val dismissIntent = Intent(ACTION_QUICK_DOWNLOAD_DISMISS).apply {
            setPackage(context.packageName)
        }

        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            NOTIFICATION_DISMISS_ID,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val dismissAction = NotificationCompat.Action.Builder(
            R.drawable.ic_clear,
            context.getString(R.string.quick_download_turn_off),
            dismissPendingIntent,
        ).build()

        val urlsSnapshot = synchronized(activeProcessingUrls) {
            activeProcessingUrls.toList()
        }
        val hasProcessingItems = urlsSnapshot.isNotEmpty()

        val contentText = if (hasProcessingItems) {
            if (urlsSnapshot.size == 1) {
                context.getString(R.string.quick_download_processing_title)
            } else {
                context.getString(R.string.quick_download_processing_multiple_title, urlsSnapshot.size)
            }
        } else {
            context.getString(R.string.quick_download_notif_desc)
        }

        val bigText = if (hasProcessingItems) {
            val info = context.getString(R.string.quick_download_processing_info)
            val urlListText = urlsSnapshot.joinToString("\n") { "$it" }
            "⏳ $info\n\n$urlListText"
        } else {
            context.getString(R.string.quick_download_notif_desc)
        }

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = launchIntent?.let {
            PendingIntent.getActivity(
                context,
                0,
                it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mever)
            .setContentTitle(context.getString(R.string.quick_download_notif_title))
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)
            .setAutoCancel(false)
            .addAction(replyAction)
            .addAction(dismissAction)
            .setDeleteIntent(dismissPendingIntent)

        if (contentPendingIntent != null) {
            builder.setContentIntent(contentPendingIntent)
        }

        if (hasProcessingItems) {
            builder.setSubText(context.getString(R.string.please_wait))
            builder.setProgress(0, 0, true)
        }

        return builder.build()
    }

    @JvmOverloads
    fun showQuickDownloadNotification(
        context: Context,
        isProcessing: Boolean = false,
        processingUrl: String? = null,
    ) {
        if (isProcessing && !processingUrl.isNullOrEmpty()) {
            activeProcessingUrls.add(processingUrl)
        } else if (!isProcessing && !processingUrl.isNullOrEmpty()) {
            activeProcessingUrls.remove(processingUrl)
        }

        val notifPermission = getNotificationPermission()
        if (notifPermission.any { context.checkSelfPermission(it) != PERMISSION_GRANTED }) {
            cancelQuickDownloadNotification(context)
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (QuickDownloadService.isRunning) {
            notificationManager.notify(NOTIFICATION_ID, buildNotification(context))
        } else {
            val intent = Intent(context, QuickDownloadService::class.java).apply {
                action = QuickDownloadService.ACTION_START
            }
            try {
                ContextCompat.startForegroundService(context, intent)
            } catch (_: Exception) {
                notificationManager.notify(NOTIFICATION_ID, buildNotification(context))
            }
        }
        requestTileUpdate(context)
    }

    fun cancelQuickDownloadNotification(context: Context) {
        QuickDownloadService.isRunning = false
        activeProcessingUrls.clear()
        val intent = Intent(context, QuickDownloadService::class.java).apply {
            action = QuickDownloadService.ACTION_STOP
        }
        try {
            context.stopService(intent)
        } catch (_: Exception) {}
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
        requestTileUpdate(context)
    }

    fun requestTileUpdate(context: Context) {
        try {
            val componentName = ComponentName(
                context.packageName,
                "com.dapascript.mever.quickdownload.QuickDownloadTileService"
            )
            TileService.requestListeningState(context.applicationContext, componentName)
        } catch (_: Exception) {}
    }
}