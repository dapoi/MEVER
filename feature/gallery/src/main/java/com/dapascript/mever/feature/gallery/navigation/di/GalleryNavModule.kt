package com.dapascript.mever.feature.gallery.navigation.di

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.feature.gallery.navigation.GalleryNavGraphImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class GalleryNavModule {

    @Binds
    @IntoSet
    abstract fun provideGalleryNavGraphImpl(galleryNavGraphImpl: GalleryNavGraphImpl): BaseNavGraph
}