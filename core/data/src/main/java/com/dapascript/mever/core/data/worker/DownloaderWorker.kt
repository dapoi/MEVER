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
import com.dapascript.mever.core.common.util.PlatformType.THREADS
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.PlatformType.VIDEY
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_SELECTED_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_CONTENTS
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_TYPE
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
class DownloaderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val moshiHelper: MoshiHelper
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork() = try {
        val link = inputData.getString(KEY_REQUEST_URL).orEmpty()
        val selectedQuality = inputData.getString(KEY_REQUEST_SELECTED_QUALITY).orEmpty()
        val type = inputData.getString(KEY_RESPONSE_TYPE) ?: "video"
        val service = apiService.getApiDownloader(link, selectedQuality, type)
        val response = workDataOf(KEY_RESPONSE_CONTENTS to moshiHelper.toJson(service))
        Result.success(response)
    } catch (e: Throwable) {
        val errorMessage = when (e) {
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is UnknownHostException -> context.getString(R.string.error_no_host)
            is IOException -> context.getString(R.string.error_io)
            is HttpException -> context.getString(R.string.error_http, e.code())
            else -> context.getString(R.string.error_unknown)
        }
        Result.failure(workDataOf(KEY_ERROR to errorMessage))
    }

    private suspend fun ApiService.getApiDownloader(
        url: String,
        selectedQuality: String,
        type: String
    ) = when (getPlatformType(url, type)) {
        FACEBOOK -> getFacebookDownloader(url).mapToEntity()
        INSTAGRAM -> getInstagramDownloader(url).mapToEntity()
        PINTEREST -> getPinterestDownloader(url).mapToEntity()
        TERABOX -> getTeraBoxDownloader(url).mapToEntity()
        THREADS -> getThreadsDownloader(url).mapToEntity()
        TIKTOK -> getTiktokDownloader(url).mapToEntity()
        TWITTER -> getTwitterDownloader(url).mapToEntity()
        VIDEY -> getVideyDownloader(url).mapToEntity()
        YOUTUBE, YOUTUBE_MUSIC -> getYoutubeDownloader(url, selectedQuality, type).mapToEntity()
        AI, ALL -> throw Throwable("Platform not supported")
    }
}