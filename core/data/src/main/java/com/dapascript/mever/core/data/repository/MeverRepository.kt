package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import kotlinx.coroutines.flow.Flow

interface MeverRepository {
    fun getAppConfig(): Flow<ApiState<AppConfigEntity>>
    fun getFacebookDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getInstagramDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getPinterestDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getTeraboxDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getThreadsDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getTiktokDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getTwitterDownloader(url: String): Flow<ApiState<List<ContentEntity>>>
    fun getYoutubeDownloader(
        url: String,
        quality: String
    ): Flow<ApiState<List<ContentEntity>>>
    fun getImageAiGenerator(prompt: String): Flow<ApiState<ImageAiEntity>>
}