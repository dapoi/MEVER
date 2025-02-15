package com.dapascript.mever.feature.setting.navigation.di

import com.dapascript.mever.core.navigation.graph.SettingNavGraph
import com.dapascript.mever.feature.setting.navigation.SettingNavGraphImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SettingNavModule {

    @Provides
    fun provideSettingNavGraph(): SettingNavGraph = SettingNavGraphImpl()
}