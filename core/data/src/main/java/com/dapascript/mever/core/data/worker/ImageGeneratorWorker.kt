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
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.util.GsonHelper.toJson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class ImageGeneratorWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: MeverRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork() = try {
        val query = inputData.getString(KEY_REQUEST_PROMPT).orEmpty()
        val state = repository.getImageAiGenerator(query).first { it !is Loading }
        when (state) {
            is Success -> {
                val response = state.data?.toJson() ?: return Result.failure(
                    workDataOf(KEY_ERROR to "Failed to generate images")
                )
                val data = workDataOf(KEY_RESPONSE_AI_IMAGES to response)
                Result.success(data)
            }

            is Error -> {
                val error = state.throwable.message ?: "Unknown error"
                Result.failure(workDataOf(KEY_ERROR to error))
            }

            else -> Result.failure(workDataOf(KEY_ERROR to "Unexpected state"))
        }
    } catch (e: Exception) {
        Result.failure(workDataOf(KEY_ERROR to e.message.orEmpty()))
    }
}