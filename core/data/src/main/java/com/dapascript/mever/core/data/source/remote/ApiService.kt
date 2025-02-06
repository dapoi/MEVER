package com.dapascript.mever.core.data.source.remote

import com.dapascript.mever.core.model.remote.FacebookDownloaderResponse
import com.dapascript.mever.core.model.remote.InstagramDownloaderResponse
import com.dapascript.mever.core.model.remote.TikTokDownloaderResponse
import com.dapascript.mever.core.model.remote.TwitterDownloaderResponse
import com.dapascript.mever.core.model.remote.YouTubeDownloaderResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("api/downloader/fbdl")
    suspend fun getFacebookDownloader(
        @Query("url") url: String
    ): FacebookDownloaderResponse

    @GET("api/downloader/igdl")
    suspend fun getInstagramDownloader(
        @Query("url") url: String
    ): InstagramDownloaderResponse

    @GET("api/downloader/twitter")
    suspend fun getTwitterDownloader(
        @Query("url") url: String
    ): TwitterDownloaderResponse

    @GET("api/downloader/ttdl")
    suspend fun getTikTokDownloader(
        @Query("url") url: String
    ): TikTokDownloaderResponse

    @GET("api/downloader/ytmp4")
    suspend fun getYoutubeDownloader(
        @Query("url") url: String,
        @Query("quality") quality: String = "480"
    ): YouTubeDownloaderResponse
}