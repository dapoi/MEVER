package com.dapascript.mever.feature.home.navigation.di

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.feature.home.navigation.HomeNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class HomeNavModule {

    @Binds
    @IntoSet
    abstract fun provideHomeNavGraph(homeNavGraphImpl: HomeNavGraphImpl): BaseNavGraph
}