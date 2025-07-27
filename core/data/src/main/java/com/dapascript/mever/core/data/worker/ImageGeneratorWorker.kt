package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.state.ApiState.Error
import com.dapascript.mever.core.common.util.state.ApiState.Loading
import com.dapascript.mever.core.common.util.state.ApiState.Success
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_AI_IMAGES
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_TOTAL_IMAGES
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.util.GsonHelper.toJson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ImageGeneratorWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: MeverRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = try {
        val prompt = inputData.getString(KEY_REQUEST_PROMPT).orEmpty()
        val totalImage = inputData.getInt(KEY_TOTAL_IMAGES, 1).coerceAtLeast(1)
        val images = mutableListOf<String>()
        repeat(totalImage) {
            val state = repository.getImageAiGenerator(prompt).first { it !is Loading }
            when (state) {
                is Success -> {
                    val response = state.data
                    response?.imagesUrl?.forEachIndexed { index, image ->
                        if (image.isNotEmpty()) images.add(image)
                    }
                }

                is Error -> {
                    val error = state.throwable.message ?: "Unknown error occurred"
                    Result.failure(workDataOf(KEY_ERROR to error))
                }

                else -> Unit
            }
        }
        if (images.isEmpty()) {
            Result.failure(workDataOf(KEY_ERROR to "No images generated"))
        }
        val response = ImageAiEntity(prompt = prompt, imagesUrl = images)
        val json = response.toJson()
        Result.success(workDataOf(KEY_RESPONSE_AI_IMAGES to json))
    } catch (e: Exception) {
        Result.failure(workDataOf(KEY_ERROR to e.message.orEmpty()))
    }
}