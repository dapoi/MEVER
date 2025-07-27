package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.base.BaseRepository
import com.dapascript.mever.core.data.source.remote.ApiService
import javax.inject.Inject

class MeverRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MeverRepository, BaseRepository() {
    override fun getAppConfig() = collectApiResult(
        fetchApi = { apiService.getAppConfig() },
        transformData = { it.mapToEntity() }
    )

    override fun getFacebookDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getFacebookDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getInstagramDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getInstagramDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getPinterestDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getPinterestDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getTeraboxDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getTeraBoxDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getTiktokDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getTiktokDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getTwitterDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getTwitterDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getYoutubeDownloader(
        url: String,
        quality: String
    ) = collectApiResult(
        fetchApi = { apiService.getYoutubeDownloader(url, quality) },
        transformData = { it.mapToEntity() }
    )


    override fun getImageAiGenerator(prompt: String) = collectApiResult(
        fetchApi = { apiService.getImageAiGenerator(prompt) },
        transformData = { it.mapToEntity() }
    )
}