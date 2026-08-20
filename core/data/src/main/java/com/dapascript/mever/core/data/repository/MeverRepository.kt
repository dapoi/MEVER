package com.dapascript.mever.core.data.repository

import android.graphics.Bitmap
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.ketch.DownloadModel
import kotlinx.coroutines.flow.Flow

interface MeverRepository {
    fun getAppConfig(): Flow<ApiState<AppConfigEntity?>>
    fun getDownloader(
        url: String,
        quality: String
    ): Flow<ApiState<List<ContentEntity>>>
    fun getImageSearch(query: String): Flow<ApiState<List<ContentEntity>>>
    fun getImageAiGenerator(prompt: String): Flow<ApiState<ImageAiEntity?>>
    fun postReportAiImage(message: String): Flow<ApiState<Unit>>
    fun uploadImage(bitmap: Bitmap, fileName: String): Flow<ApiState<String?>>
    fun observeDownloads(): Flow<List<DownloadModel>>
    fun download(
        url: String,
        fileName: String,
        tag: String,
        thumbnail: String = ""
    )
    fun pauseDownload(id: Int)
    fun resumeDownload(id: Int)
    fun retryDownload(id: Int)
    fun pauseAllDownloads()
    fun deleteDownload(id: Int)
    fun deleteDownloads(ids: List<Int>)
    fun deleteAllDownloads()
    suspend fun refreshDownloadDatabase()
}