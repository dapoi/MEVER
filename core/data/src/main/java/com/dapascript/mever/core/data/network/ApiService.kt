package com.dapascript.mever.core.data.network

import com.dapascript.mever.core.model.network.GetVideoDownloaderResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/downloader/aiodown")
    suspend fun getVideoDownloader(
        @Query("url") url: String
    ): GetVideoDownloaderResponse
}