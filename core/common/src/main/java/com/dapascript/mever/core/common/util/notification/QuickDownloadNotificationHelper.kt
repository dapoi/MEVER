package com.dapascript.mever.core.common.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.dapascript.mever.core.common.R

object QuickDownloadNotificationHelper {

    private const val CHANNEL_ID = "quick_download_channel_silent"
    private const val CHANNEL_NAME = "Quick Download"
    const val ACTION_QUICK_DOWNLOAD_REPLY = "com.dapascript.mever.ACTION_QUICK_DOWNLOAD_REPLY"
    const val KEY_TEXT_REPLY = "key_quick_download_text"
    const val NOTIFICATION_ID = 23041

    fun showQuickDownloadNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW,
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
            replyPendingIntent,
        )
            .addRemoteInput(remoteInput)
            .build()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mever)
            .setContentTitle(context.getString(R.string.quick_download_notif_title))
            .setContentText(context.getString(R.string.quick_download_notif_desc))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)
            .setSilent(true)
            .addAction(replyAction)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun cancelQuickDownloadNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
