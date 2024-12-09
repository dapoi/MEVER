package com.dapascript.mever.core.common.navigation.di

import com.dapascript.mever.core.common.navigation.MeverNavGraphs
import com.dapascript.mever.core.common.navigation.graph.GalleryNavGraph
import com.dapascript.mever.core.common.navigation.graph.HomeNavGraph
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.navigation.graph.StartupNavGraph
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MeverNavGraphsModule {

    @Provides
    fun provideMeverNavGraphs(
        startupNavGraph: StartupNavGraph,
        homeNavGraph: HomeNavGraph,
        notificationNavGraph: NotificationNavGraph,
        settingNavGraph: SettingNavGraph,
        galleryNavGraph: GalleryNavGraph
    ) = MeverNavGraphs(
        listOf(
            startupNavGraph,
            homeNavGraph,
            notificationNavGraph,
            settingNavGraph,
            galleryNavGraph
        )
    )
}