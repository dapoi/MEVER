package com.dapascript.mever.feature.home.navigation.di

import com.dapascript.mever.feature.home.navigation.HomeNavGraphImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HomeNavModule {

    @Provides
    fun provideHomeNavGraph() = HomeNavGraphImpl()
}