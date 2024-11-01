package com.dapascript.mever.core.common.navigation.di

import com.dapascript.mever.core.common.navigation.graph.HomeNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.navigation.MeverNavGraphs
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MeverNavGraphsModule {

    @Provides
    fun provideMeverNavGraphs(
        homeNavGraph: HomeNavGraph,
        notificationNavGraph: NotificationNavGraph,
        settingNavGraph: SettingNavGraph
    ) = MeverNavGraphs(
        listOf(
            homeNavGraph,
            notificationNavGraph,
            settingNavGraph
        )
    )
}