package com.dapascript.mever.feature.gallery.navigation.di

import com.dapascript.mever.feature.gallery.navigation.GalleryNavGraphImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GalleryNavModule {

    @Provides
    fun provideGalleryNavGraphImpl() = GalleryNavGraphImpl()
}