package com.dapascript.mever.di

import android.app.Application
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserverImpl
import com.dapascript.mever.core.navigation.MeverNavGraphs
import com.dapascript.mever.feature.gallery.navigation.GalleryNavGraphImpl
import com.dapascript.mever.feature.home.navigation.HomeNavGraphImpl
import com.dapascript.mever.feature.setting.navigation.SettingNavGraphImpl
import com.dapascript.mever.feature.startup.navigation.StartupNavGraphImpl
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
    fun provideNavGraphs(
        galleryNavGraphImpl: GalleryNavGraphImpl,
        homeNavGraphImpl: HomeNavGraphImpl,
        settingNavGraphImpl: SettingNavGraphImpl,
        startupNavGraphImpl: StartupNavGraphImpl
    ) = MeverNavGraphs(
        listOf(
            startupNavGraphImpl,
            galleryNavGraphImpl,
            homeNavGraphImpl,
            settingNavGraphImpl
        )
    )

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