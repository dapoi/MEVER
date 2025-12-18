package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_AI_IMAGES
import com.dapascript.mever.core.data.R
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.worker.base.BaseWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.reflect.Type

@HiltWorker
class ImageGeneratorWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    moshiHelper: MoshiHelper,
    private val apiService: ApiService
) : BaseWorker<ImageAiEntity>(context, workerParameters, moshiHelper) {

    override val outputSuccessKey: String = KEY_RESPONSE_AI_IMAGES
    override val resultType: Type = ImageAiEntity::class.java

    override suspend fun doApiCall(): ImageAiEntity {
        val prompt = inputData.getString(KEY_REQUEST_PROMPT).orEmpty()
        return apiService.getImageAiGenerator(
            if (prompt.contains("gambar", ignoreCase = true)) prompt
            else "Buatkan gambar atau image: $prompt"
        ).mapToEntity().let { content ->
            if (content.imagesUrl.isNotEmpty()) content
            else throw Throwable(context.getString(R.string.error_unknown))
        }
    }
}