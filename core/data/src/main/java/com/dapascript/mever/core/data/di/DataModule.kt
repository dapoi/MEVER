package com.dapascript.mever.core.data.di

import android.content.Context
import androidx.work.WorkManager
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dapascript.mever.core.data.BuildConfig
import com.dapascript.mever.core.data.BuildConfig.BASE_URL
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.MeverRepositoryImpl
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.ApiKeyInterceptor
import com.dapascript.mever.core.data.util.MoshiHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.MINUTES

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideMoshiHelper(moshi: Moshi) = MoshiHelper(moshi)

    @Provides
    fun provideApiKeyInterceptor(): ApiKeyInterceptor = ApiKeyInterceptor()

    @Provides
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context
    ): ChuckerInterceptor = ChuckerInterceptor.Builder(context)
        .collector(ChuckerCollector(context))
        .build()

    @Provides
    fun provideOkHttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) BODY else NONE
        })
        .addInterceptor(apiKeyInterceptor)
        .addInterceptor(chuckerInterceptor)
        .connectTimeout(1, MINUTES)
        .readTimeout(1, MINUTES)
        .writeTimeout(1, MINUTES)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit = Retrofit.Builder()
        .baseUrl("$BASE_URL/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideMeverRepository(
        apiService: ApiService,
        workManager: WorkManager,
        moshiHelper: MoshiHelper
    ): MeverRepository = MeverRepositoryImpl(
        apiService = apiService,
        workManager = workManager,
        moshiHelper = moshiHelper
    )

    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)
}