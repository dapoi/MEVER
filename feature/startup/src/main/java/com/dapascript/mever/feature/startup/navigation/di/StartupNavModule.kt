package com.dapascript.mever.feature.startup.navigation.di

import com.dapascript.mever.core.common.navigation.graph.StartupNavGraph
import com.dapascript.mever.feature.startup.navigation.StartupNavGraphImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class StartupNavModule {

    @Provides
    fun provideStartupNavGraph(): StartupNavGraph = StartupNavGraphImpl()
}