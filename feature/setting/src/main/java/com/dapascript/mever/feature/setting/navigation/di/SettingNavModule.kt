package com.dapascript.mever.feature.setting.navigation.di

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.feature.setting.navigation.SettingNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingNavModule {

    @Binds
    @IntoSet
    abstract fun provideSettingNavGraph(settingNavGraphImpl: SettingNavGraphImpl): BaseNavGraph
}