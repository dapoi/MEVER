package com.dapascript.mever.core.data.repository

import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_DOWNLOAD
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_GENERATE_AI
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_TYPE
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.TYPE_AUDIO
import com.dapascript.mever.core.common.util.worker.WorkerConstant.TYPE_VIDEO
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.base.BaseRepository
import com.dapascript.mever.core.data.repository.base.BaseRepositoryArgs
import com.dapascript.mever.core.data.source.remote.ApiService
import javax.inject.Inject

class MeverRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    args: BaseRepositoryArgs
) : MeverRepository, BaseRepository(args) {

    override fun getAppConfig() = safeApiCall {
        apiService.getAppConfig().mapToEntity()
    }

    override fun getDownloader(
        url: String,
        quality: String
    ) = safeWorkerCall<List<ContentEntity>>(
        serviceType = ACTION_DOWNLOAD,
        requestParam = workDataOf(
            KEY_URL to url,
            KEY_QUALITY to quality,
            KEY_TYPE to if (quality.contains("kbps", true)) TYPE_AUDIO else TYPE_VIDEO
        )
    )

    override fun getImageSearch(query: String) = safeApiCall {
        apiService.getImageSearch(query).mapToEntity() ?: emptyList()
    }

    override fun getImageAiGenerator(prompt: String) = safeWorkerCall<ImageAiEntity>(
        serviceType = ACTION_GENERATE_AI,
        requestParam = workDataOf(
            KEY_PROMPT to prompt
        )
    )

    override fun postReportAiImage(message: String) = safeApiCall {
        apiService.reportAiImage(message)
    }
}