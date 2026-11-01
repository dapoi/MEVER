package com.dapascript.mever.core.common.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.getNotificationPermission
import java.util.Collections
import java.util.LinkedHashSet

object QuickDownloadNotificationHelper {

    private const val CHANNEL_ID = "quick_download_channel_silent"
    private const val CHANNEL_NAME = "Quick Download"
    const val ACTION_QUICK_DOWNLOAD_REPLY = "com.dapascript.mever.ACTION_QUICK_DOWNLOAD_REPLY"
    const val KEY_TEXT_REPLY = "key_quick_download_text"
    const val NOTIFICATION_ID = 23041

    private val activeProcessingUrls = Collections.synchronizedSet(LinkedHashSet<String>())

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
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Channel for Quick Download input notification"
            setSound(null, null)
            enableVibration(false)
        }
        notificationManager.createNotificationChannel(channel)

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
            replyPendingIntent
        )
            .addRemoteInput(remoteInput)
            .build()

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
            val urlListText = urlsSnapshot.joinToString("\n") { "• $it" }
            "⏳ $info\n\n$urlListText"
        } else {
            context.getString(R.string.quick_download_notif_desc)
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

        if (hasProcessingItems) {
            builder.setSubText(context.getString(R.string.please_wait))
            builder.setProgress(0, 0, true)
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun cancelQuickDownloadNotification(context: Context) {
        activeProcessingUrls.clear()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
