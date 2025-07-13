package com.dapascript.mever.core.data.source.remote

import com.dapascript.mever.core.data.model.remote.FacebookDownloaderResponse
import com.dapascript.mever.core.data.model.remote.ImageAiResponse
import com.dapascript.mever.core.data.model.remote.SavefromDownloaderResponse
import com.dapascript.mever.core.data.model.remote.YouTubeDownloaderResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("savefrom")
    suspend fun getSaveFromDownloader(
        @Query("url") url: String
    ): SavefromDownloaderResponse

    @GET("fb")
    suspend fun getFacebookDownloader(
        @Query("url") url: String
    ): FacebookDownloaderResponse

    @GET("youtube")
    suspend fun getYoutubeDownloader(
        @Query("url") url: String,
        @Query("quality") quality: String
    ): YouTubeDownloaderResponse

    @GET("meta")
    suspend fun getImageAiGenerator(
        @Query("q") query: String
    ): ImageAiResponse
}