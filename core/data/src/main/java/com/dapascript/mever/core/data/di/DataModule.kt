package com.dapascript.mever.core.data.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dapascript.mever.core.data.BuildConfig
import com.dapascript.mever.core.data.BuildConfig.API_KEY
import com.dapascript.mever.core.data.BuildConfig.BASE_URL
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.MeverRepositoryImpl
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.ApiKey
import com.dapascript.mever.core.data.util.ApiKeyInterceptor
import com.dapascript.mever.core.data.util.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.MINUTES

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @ApiKey
    fun provideApiKey(): String = API_KEY

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = BASE_URL

    @Provides
    fun provideApiKeyInterceptor(
        @ApiKey apiKey: String
    ) = ApiKeyInterceptor(apiKey)

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideApiService(
        @BaseUrl baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) BODY else NONE
        })
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(chuckerInterceptor)
        .connectTimeout(1, MINUTES)
        .readTimeout(1, MINUTES)
        .writeTimeout(1, MINUTES)
        .build()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context
    ) = ChuckerInterceptor.Builder(context).collector(ChuckerCollector(context)).build()

    @Provides
    fun provideMeverDataStore(@ApplicationContext context: Context) = MeverDataStore(context)

    @Provides
    fun provideMeverRepository(repo: MeverRepositoryImpl): MeverRepository = repo
}