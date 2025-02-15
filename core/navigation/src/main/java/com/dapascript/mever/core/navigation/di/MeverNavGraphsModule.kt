package com.dapascript.mever.core.navigation.di

import com.dapascript.mever.core.navigation.MeverNavGraphs
import com.dapascript.mever.core.navigation.graph.GalleryNavGraph
import com.dapascript.mever.core.navigation.graph.HomeNavGraph
import com.dapascript.mever.core.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.navigation.graph.StartupNavGraph
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
        galleryNavGraph: GalleryNavGraph,
        homeNavGraph: HomeNavGraph,
        settingNavGraph: SettingNavGraph
    ) = MeverNavGraphs(
        listOf(
            startupNavGraph,
            galleryNavGraph,
            homeNavGraph,
            settingNavGraph
        )
    )
}