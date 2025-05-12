package com.dapascript.mever.core.data.source.remote

import com.dapascript.mever.core.data.model.remote.FacebookDownloaderResponse
import com.dapascript.mever.core.data.model.remote.InstagramDownloaderResponse
import com.dapascript.mever.core.data.model.remote.SavefromDownloaderResponse
import com.dapascript.mever.core.data.model.remote.TikTokDownloaderResponse
import com.dapascript.mever.core.data.model.remote.TwitterDownloaderResponse
import com.dapascript.mever.core.data.model.remote.YouTubeDownloaderResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("api/savefrom")
    suspend fun getSaveFromDownloader(
        @Query("url") url: String
    ): SavefromDownloaderResponse

    @GET("api/fb")
    suspend fun getFacebookDownloader(
        @Query("url") url: String
    ): FacebookDownloaderResponse

    @GET("api/youtube")
    suspend fun getYoutubeDownloader(
        @Query("url") url: String,
        @Query("quality") quality: String,
        @Query("type") type: String = "video",
    ): YouTubeDownloaderResponse

    @GET("api/ig")
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
}