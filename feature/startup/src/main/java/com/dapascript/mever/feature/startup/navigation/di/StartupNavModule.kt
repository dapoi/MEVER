package com.dapascript.mever.feature.startup.navigation.di

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.feature.startup.navigation.StartupNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class StartupNavModule {

    @Binds
    @IntoSet
    abstract fun provideStartupNavGraph(startupNavGraphImpl: StartupNavGraphImpl): BaseNavGraph
}