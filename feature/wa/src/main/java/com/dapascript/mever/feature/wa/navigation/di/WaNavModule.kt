package com.dapascript.mever.feature.wa.navigation.di

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.feature.wa.navigation.WaNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class WaNavModule {

    @Binds
    @IntoSet
    abstract fun bindWaNavGraph(impl: WaNavGraphImpl): BaseNavGraph
}