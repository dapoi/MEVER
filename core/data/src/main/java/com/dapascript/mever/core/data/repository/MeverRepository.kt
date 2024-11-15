package com.dapascript.mever.core.data.repository

import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.model.local.VideoUrlEntity
import kotlinx.coroutines.flow.Flow

interface MeverRepository {
    fun getVideoDownloader(url: String): Flow<ApiState<List<VideoUrlEntity>>>
}