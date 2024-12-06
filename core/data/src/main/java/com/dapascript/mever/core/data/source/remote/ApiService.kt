package com.dapascript.mever.core.data.source.remote

import com.dapascript.mever.core.model.remote.FacebookDownloaderResponse
import com.dapascript.mever.core.model.remote.InstagramDownloaderResponse
import com.dapascript.mever.core.model.remote.TikTokDownloaderResponse
import com.dapascript.mever.core.model.remote.TwitterDownloaderResponse
import retrofit2.http.GET
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

    @GET("api/downloader/v2/ttdl")
    suspend fun getTikTokDownloader(
        @Query("url") url: String
    ): TikTokDownloaderResponse
}