package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.TERABOX
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.state.ApiState.Loading
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_SELECTED_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_CONTENTS
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.util.GsonHelper.toJson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class DownloaderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val repository: MeverRepository
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork() = try {
        val link = inputData.getString(KEY_REQUEST_URL).orEmpty()
        val selectedQuality = inputData.getString(KEY_REQUEST_SELECTED_QUALITY).orEmpty()
        val state = repository.getApiDownloader(link, selectedQuality).first {
            it !is Loading
        }
        when (state) {
            is ApiState.Success -> {
                val response = state.data?.toJson() ?: Result.failure(
                    workDataOf(KEY_ERROR to "No contents found")
                )
                val data = workDataOf(KEY_RESPONSE_CONTENTS to response)
                Result.success(data)
            }

            is ApiState.Error -> {
                val error = state.throwable.message
                Result.failure(workDataOf(KEY_ERROR to error))
            }

            else -> Result.failure(workDataOf(KEY_ERROR to "Unexpected state"))
        }
    } catch (e: Throwable) {
        Result.failure(workDataOf(KEY_ERROR to e.message))
    }

    private fun MeverRepository.getApiDownloader(
        url: String,
        selectedQuality: String
    ) = when (getPlatformType(url)) {
        FACEBOOK -> getFacebookDownloader(url)
        INSTAGRAM -> getInstagramDownloader(url)
        PINTEREST -> getPinterestDownloader(url)
        TERABOX -> getTeraboxDownloader(url)
        TIKTOK -> getTiktokDownloader(url)
        TWITTER -> getTwitterDownloader(url)
        YOUTUBE -> getYoutubeDownloader(url, selectedQuality)
        AI, ALL -> throw Throwable("Platform not supported")
    }
}