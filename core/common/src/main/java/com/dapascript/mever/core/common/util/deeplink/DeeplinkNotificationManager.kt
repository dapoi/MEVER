package com.dapascript.mever.core.common.util.deeplink

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.net.toUri
import com.dapascript.mever.core.common.R

object DeeplinkNotificationManager {
    private const val MAIN_ACTIVITY = "com.dapascript.mever.screen.MainActivity"
    private const val CHANNEL_ID = "deeplink_notification_channel"
    private const val CHANNEL_NAME = "Deeplink Notifications"
    const val NOTIFICATION_ID = 12021

    fun showNotification(
        context: Context,
        title: String,
        desc: String,
        deeplink: String
    ) {
        val notificationManager = context.getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
        val intent = Intent(
            ACTION_VIEW,
            deeplink.toUri(),
            context,
            Class.forName(MAIN_ACTIVITY)
        ).apply { flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_SINGLE_TOP }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mever)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}