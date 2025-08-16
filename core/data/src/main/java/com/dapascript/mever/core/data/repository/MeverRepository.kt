package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import kotlinx.coroutines.flow.Flow

interface MeverRepository {
    fun getAppConfig(): Flow<ApiState<AppConfigEntity?>>
    fun getDownloader(
        url: String,
        quality: String
    ): Flow<ApiState<List<ContentEntity>>>
    fun getImageAiGenerator(prompt: String): Flow<ApiState<ImageAiEntity?>>
}