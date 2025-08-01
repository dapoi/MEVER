package com.dapascript.mever.core.data.source.remote

import com.dapascript.mever.core.data.model.remote.AppConfigResponse
import com.dapascript.mever.core.data.model.remote.FacebookDownloaderResponse
import com.dapascript.mever.core.data.model.remote.ImageAiResponse
import com.dapascript.mever.core.data.model.remote.InstagramDownloaderResponse
import com.dapascript.mever.core.data.model.remote.PinterestDownloaderResponse
import com.dapascript.mever.core.data.model.remote.TeraboxDownloaderResponse
import com.dapascript.mever.core.data.model.remote.TiktokDownloaderResponse
import com.dapascript.mever.core.data.model.remote.TwitterDownloaderResponse
import com.dapascript.mever.core.data.model.remote.YouTubeDownloaderResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("app-config")
    suspend fun getAppConfig(): AppConfigResponse

    @GET("fb")
    suspend fun getFacebookDownloader(
        @Query("url") url: String
    ): FacebookDownloaderResponse

    @GET("ig")
    suspend fun getInstagramDownloader(
        @Query("url") url: String
    ): InstagramDownloaderResponse

    @GET("pin-v2")
    suspend fun getPinterestDownloader(
        @Query("url") url: String
    ): PinterestDownloaderResponse

    @GET("terabox")
    suspend fun getTeraBoxDownloader(
        @Query("url") url: String
    ): TeraboxDownloaderResponse

    @GET("tiktok")
    suspend fun getTiktokDownloader(
        @Query("url") url: String
    ): TiktokDownloaderResponse

    @GET("twitter")
    suspend fun getTwitterDownloader(
        @Query("url") url: String
    ): TwitterDownloaderResponse

    @GET("youtube")
    suspend fun getYoutubeDownloader(
        @Query("url") url: String,
        @Query("quality") quality: String,
        @Query("type") type: String = "video"
    ): YouTubeDownloaderResponse

    @GET("meta")
    suspend fun getImageAiGenerator(
        @Query("q") prompt: String,
        @Query("session") session: String = "bb286368-37d4-485d-9522-fb88ee8f92b4",
        @Query("lang") lang: String = "id"
    ): ImageAiResponse
}