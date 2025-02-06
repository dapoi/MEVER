package com.dapascript.mever

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
open class MeverApp : Application() {

//    lateinit var ketch: Ketch
//        private set
//
//    lateinit var connectivityObserver: ConnectivityObserver
//        private set

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

//        ketch = Ketch.builder()
//            .setNotificationConfig(
//                NotificationConfig(
//                    enabled = true,
//                    smallIcon = R.drawable.ic_mever
//                )
//            )
//            .setDownloadConfig(
//                DownloadConfig(
//                    connectTimeOutInMs = 10000,
//                    readTimeOutInMs = 10000
//                )
//            )
//            .enableLogs(true)
//            .build(this)
//
//        connectivityObserver = ConnectivityObserverImpl(this)
    }
}