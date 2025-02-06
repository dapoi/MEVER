package com.dapascript.mever.di

import android.app.Application
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserverImpl
import com.ketch.DownloadConfig
import com.ketch.Ketch
import com.ketch.NotificationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideKetch(application: Application) = Ketch.builder()
        .setNotificationConfig(
            NotificationConfig(
                enabled = true,
                smallIcon = R.drawable.ic_mever
            )
        )
        .setDownloadConfig(
            DownloadConfig(
                connectTimeOutInMs = 10000,
                readTimeOutInMs = 10000
            )
        )
        .enableLogs(true)
        .build(application)

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        application: Application
    ): ConnectivityObserver = ConnectivityObserverImpl(application)
}