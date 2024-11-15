package com.dapascript.mever.core.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dapascript.mever.core.data.network.ApiService
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.MeverRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideMeverRepository(repo: MeverRepositoryImpl): MeverRepository = repo

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiService(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.ryzendesu.vip/")
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(chuckerInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context
    ) = ChuckerInterceptor.Builder(context).collector(ChuckerCollector(context)).build()
}