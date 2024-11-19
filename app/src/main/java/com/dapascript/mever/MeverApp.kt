package com.dapascript.mever

import android.app.Application
import android.app.NotificationManager.IMPORTANCE_MAX
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserverImpl
import com.ketch.DownloadConfig
import com.ketch.Ketch
import com.ketch.NotificationConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class MeverApp : Application() {

    lateinit var ketch: Ketch
        private set

    lateinit var connectivityObserver: ConnectivityObserver
        private set

    override fun onCreate() {
        super.onCreate()

        ketch = Ketch.builder()
            .setNotificationConfig(
                NotificationConfig(
                    enabled = true,
                    smallIcon = R.drawable.ic_mever,
                    importance = IMPORTANCE_MAX
                )
            )
            .setDownloadConfig(
                DownloadConfig(
                    connectTimeOutInMs = 10000,
                    readTimeOutInMs = 10000
                )
            )
            .build(this)

        connectivityObserver = ConnectivityObserverImpl(this)
    }
}