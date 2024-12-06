package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.model.local.VideoGeneralEntity
import kotlinx.coroutines.flow.Flow

interface MeverRepository {
    fun getFacebookDownloader(url: String): Flow<ApiState<List<VideoGeneralEntity>>>
    fun getInstagramDownloader(url: String): Flow<ApiState<List<VideoGeneralEntity>>>
    fun getTwitterDownloader(url: String): Flow<ApiState<List<VideoGeneralEntity>>>
    fun getTikTokDownloader(url: String): Flow<ApiState<List<VideoGeneralEntity>>>
}