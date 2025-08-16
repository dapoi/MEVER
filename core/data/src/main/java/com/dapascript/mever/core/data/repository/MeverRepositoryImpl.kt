package com.dapascript.mever.core.data.repository

import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_SELECTED_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_AI_IMAGES
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_APP_CONFIG
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_CONTENTS
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_TYPE
import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.base.BaseRepository
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.worker.AppConfigWorker
import com.dapascript.mever.core.data.worker.DownloaderWorker
import com.dapascript.mever.core.data.worker.ImageGeneratorWorker
import javax.inject.Inject

class MeverRepositoryImpl @Inject constructor(
    private val workManager: WorkManager,
    private val moshiHelper: MoshiHelper
) : MeverRepository, BaseRepository() {

    override fun getAppConfig() = collectApiResultWithWorker(
        workManager = workManager,
        workerClass = AppConfigWorker::class.java,
        responses = {
            val data = it.getString(KEY_RESPONSE_APP_CONFIG).orEmpty()
            moshiHelper.fromJson<AppConfigEntity>(data)
        }
    )

    override fun getDownloader(
        url: String,
        quality: String
    ) = collectApiResultWithWorker(
        workManager = workManager,
        workerClass = DownloaderWorker::class.java,
        requestParam = workDataOf(
            KEY_REQUEST_URL to url,
            KEY_REQUEST_SELECTED_QUALITY to quality,
            KEY_RESPONSE_TYPE to if (quality.contains("kbps", true)) "audio" else "video"
        ),
        responses = {
            val data = it.getString(KEY_RESPONSE_CONTENTS).orEmpty()
            moshiHelper.fromJson<List<ContentEntity>>(data) ?: emptyList()
        }
    )

    override fun getImageAiGenerator(prompt: String) = collectApiResultWithWorker(
        workManager = workManager,
        workerClass = ImageGeneratorWorker::class.java,
        requestParam = workDataOf(KEY_REQUEST_PROMPT to prompt),
        responses = {
            val data = it.getString(KEY_RESPONSE_AI_IMAGES).orEmpty()
            moshiHelper.fromJson<ImageAiEntity>(data)
        }
    )
}