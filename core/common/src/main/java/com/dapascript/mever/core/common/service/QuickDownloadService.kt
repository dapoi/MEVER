package com.dapascript.mever.core.common.service

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.ServiceCompat
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.NOTIFICATION_ID

class QuickDownloadService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopForegroundService()
            return START_NOT_STICKY
        }

        startForegroundNotification()
        return START_STICKY
    }

    private fun startForegroundNotification() {
        val notifPermission = getNotificationPermission()
        if (notifPermission.any { checkSelfPermission(it) != PERMISSION_GRANTED }) {
            stopForegroundService()
            return
        }

        val notification = QuickDownloadNotificationHelper.buildNotification(this)
        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        } else {
            0
        }
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            foregroundServiceType,
        )
    }

    private fun stopForegroundService() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        isRunning = false
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    companion object {
        const val ACTION_START = "com.dapascript.mever.action.START_QUICK_DOWNLOAD"
        const val ACTION_STOP = "com.dapascript.mever.action.STOP_QUICK_DOWNLOAD"

        @Volatile
        var isRunning: Boolean = false
            internal set
    }
}
