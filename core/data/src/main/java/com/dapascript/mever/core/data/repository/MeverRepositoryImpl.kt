package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.base.BaseRepository
import com.dapascript.mever.core.data.source.remote.ApiService
import javax.inject.Inject

class MeverRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MeverRepository, BaseRepository() {

    override fun getFacebookDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getFacebookDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getInstagramDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getInstagramDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getTwitterDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getTwitterDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getTikTokDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getTikTokDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getYoutubeDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getYoutubeDownloader(url) },
        transformData = { it.mapToEntity() }
    )
}