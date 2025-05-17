package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.base.BaseRepository
import com.dapascript.mever.core.data.BuildConfig.SESSION
import com.dapascript.mever.core.data.source.remote.ApiService
import javax.inject.Inject

class MeverRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MeverRepository, BaseRepository() {
    override fun getSavefromDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getSaveFromDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getFacebookDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getFacebookDownloader(url) },
        transformData = { it.mapToEntity() }
    )

    override fun getYoutubeDownloader(
        url: String,
        quality: String
    ) = collectApiResult(
        fetchApi = { apiService.getYoutubeDownloader(url, quality) },
        transformData = { it.mapToEntity() }
    )

    override fun getImageAiGenerator(query: String) = collectApiResult(
        fetchApi = { apiService.getImageAiGenerator(query, SESSION) },
        transformData = { it.mapToEntity() }
    )
}