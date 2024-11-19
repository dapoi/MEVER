package com.dapascript.mever.di

import android.app.Application
import com.dapascript.mever.MeverApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideKetch(application: Application) = (application as MeverApp).ketch

    @Provides
    @Singleton
    fun provideConnectivityObserver(application: Application) = (application as MeverApp).connectivityObserver
}