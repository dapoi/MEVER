package com.dapascript.mever.core.data.repository

import android.content.Context
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_SELECTED_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_AI_IMAGES
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_CONTENTS
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_TYPE
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.base.BaseRepository
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.worker.DownloaderWorker
import com.dapascript.mever.core.data.worker.ImageGeneratorWorker
import javax.inject.Inject

class MeverRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val workManager: WorkManager,
    private val moshiHelper: MoshiHelper
) : MeverRepository, BaseRepository() {
    override fun getAppConfig(context: Context) = collectApiResult(
        context = context,
        fetchApi = { apiService.getAppConfig() },
        transformData = { it.mapToEntity() }
    )

    override fun getDownloader(
        url: String,
        quality: String
    ) = collectApiResultWithWorker(
        workManager = workManager,
        workerClass = DownloaderWorker::class.java,
        inputData = workDataOf(
            KEY_REQUEST_URL to url,
            KEY_REQUEST_SELECTED_QUALITY to quality,
            KEY_RESPONSE_TYPE to if (quality.contains("kbps", true)) "audio" else "video"
        ),
        output = {
            val data = it.getString(KEY_RESPONSE_CONTENTS).orEmpty()
            moshiHelper.fromJson<List<ContentEntity>>(data) ?: emptyList()
        }
    )

    override fun getImageAiGenerator(prompt: String) = collectApiResultWithWorker(
        workManager = workManager,
        workerClass = ImageGeneratorWorker::class.java,
        inputData = workDataOf(KEY_REQUEST_PROMPT to prompt),
        output = {
            val data = it.getString(KEY_RESPONSE_AI_IMAGES).orEmpty()
            val response = moshiHelper.fromJson<ImageAiEntity>(data)
            ImageAiEntity(
                prompt = response?.prompt.orEmpty(),
                imagesUrl = response?.imagesUrl.orEmpty()
            )
        }
    )
}