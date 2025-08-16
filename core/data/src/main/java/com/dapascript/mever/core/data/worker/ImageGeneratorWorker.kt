package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_AI_IMAGES
import com.dapascript.mever.core.data.R
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@HiltWorker
class ImageGeneratorWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val moshiHelper: MoshiHelper
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result = try {
        val prompt = inputData.getString(KEY_REQUEST_PROMPT).orEmpty()
        val service = apiService.getImageAiGenerator(prompt).mapToEntity()
        val response = workDataOf(KEY_RESPONSE_AI_IMAGES to moshiHelper.toJson(service))
        Result.success(response)
    } catch (e: Exception) {
        val errorMessage = when (e) {
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is UnknownHostException -> context.getString(R.string.error_no_host)
            is IOException -> context.getString(R.string.error_io)
            is HttpException -> context.getString(R.string.error_http, e.code())
            else -> context.getString(R.string.error_unknown)
        }
        Result.failure(workDataOf(KEY_ERROR to errorMessage))
    }
}