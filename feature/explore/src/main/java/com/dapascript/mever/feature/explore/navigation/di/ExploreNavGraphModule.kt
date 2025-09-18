package com.dapascript.mever.feature.explore.navigation.di

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.feature.explore.navigation.ExploreNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class ExploreNavGraphModule {

    @Binds
    @IntoSet
    abstract fun provideExploreNavGraph(exploreNavGraphImpl: ExploreNavGraphImpl): BaseNavGraph
}