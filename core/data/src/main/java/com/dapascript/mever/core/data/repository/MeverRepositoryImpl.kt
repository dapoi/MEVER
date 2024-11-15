package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.base.BaseRepository
import com.dapascript.mever.core.data.network.ApiService
import javax.inject.Inject

class MeverRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : MeverRepository, BaseRepository() {
    override fun getVideoDownloader(url: String) = collectApiResult(
        fetchApi = { apiService.getVideoDownloader(url) },
        transformData = { it.mapToEntity() }
    )
}