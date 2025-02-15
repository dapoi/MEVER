package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.data.model.local.ContentEntity
import kotlinx.coroutines.flow.Flow

interface MeverRepository {
    fun getFacebookDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getInstagramDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getTwitterDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getTikTokDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getYoutubeDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
}