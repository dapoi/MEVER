package com.dapascript.mever.feature.ai.navigation.di

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.feature.ai.navigation.AiNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class AiNavGraphModule {

    @Binds
    @IntoSet
    abstract fun provideAiNavGraph(aiNavGraphImpl: AiNavGraphImpl): BaseNavGraph
}