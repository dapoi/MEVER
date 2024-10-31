package com.dapascript.mever.feature.notification.navigation.di

import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.feature.notification.navigation.NotificationNavGraphImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NotificationNavModule {

    @Provides
    fun provideNotificationNavGraph(): NotificationNavGraph = NotificationNavGraphImpl()
}