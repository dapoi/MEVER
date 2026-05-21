package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.PlatformType.*
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_DOWNLOAD
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_GENERATE_AI
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ACTION
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_FILE_PATH
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_IS_FILE
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESULT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_TYPE
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.SIZE_LIMIT
import com.dapascript.mever.core.data.R
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import com.squareup.moshi.Types
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@HiltWorker
class MeverWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val moshiHelper: MoshiHelper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = try {
        val action = inputData.getString(KEY_ACTION)

        val (resultData, resultType) = when (action) {
            ACTION_DOWNLOAD -> {
                val url = inputData.getString(KEY_URL).orEmpty()
                val quality = inputData.getString(KEY_QUALITY).orEmpty()
                val type = inputData.getString(KEY_TYPE) ?: "video"
                val res = getApiDownloader(url, quality, type).let { list ->
                    if (list.firstOrNull()?.status == true) list else emptyList()
                }
                res to Types.newParameterizedType(List::class.java, ContentEntity::class.java)
            }

            ACTION_GENERATE_AI -> {
                apiService.getImageAiGenerator(inputData.getString(KEY_PROMPT).orEmpty())
                    .mapToEntity() to ImageAiEntity::class.java
            }

            else -> throw IllegalArgumentException("Unknown action: $action")
        }

        val jsonOutput = moshiHelper.toJson(resultType, resultData)
        val size = jsonOutput?.toByteArray()?.size ?: 0

        if (size > SIZE_LIMIT) {
            val path = context.cacheDir
                .resolve("$KEY_RESULT.json")
                .apply { writeText(jsonOutput!!) }.absolutePath
            Result.success(workDataOf(KEY_OUTPUT_IS_FILE to true, KEY_OUTPUT_FILE_PATH to path))
        } else {
            Result.success(workDataOf(KEY_OUTPUT_IS_FILE to false, KEY_RESULT to jsonOutput))
        }
    } catch (e: Exception) {
        val errorMessage = when (e) {
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is UnknownHostException -> context.getString(R.string.error_no_host)
            is IOException -> context.getString(R.string.error_io)
            is HttpException -> context.getString(R.string.error_http, e.code())
            else -> e.message ?: context.getString(R.string.error_unknown)
        }
        Result.failure(workDataOf(KEY_ERROR to errorMessage))
    }

    private suspend fun getApiDownloader(
        url: String,
        quality: String,
        type: String
    ): List<ContentEntity> =
        when (getPlatformType(url, type)) {
            APPLE_MUSIC -> apiService.getAppleMusicDownloader(url).mapToEntity()
            DOUYIN -> apiService.getDouyinDownloader(url).mapToEntity()
            FACEBOOK -> apiService.getFacebookDownloader(url).mapToEntity() ?: emptyList()
            INSTAGRAM -> apiService.getInstagramDownloader(url).mapToEntity() ?: emptyList()
            PINTEREST -> apiService.getPinterestDownloader(url).mapToEntity() ?: emptyList()
            PIXIV -> apiService.getPixivDownloader(url).mapToEntity() ?: emptyList()
            SOUNDCLOUD -> apiService.getSoundCloudDownloader(url).mapToEntity()
            SPOTIFY -> apiService.getSpotifyDownloader(url).mapToEntity() ?: emptyList()
            TERABOX -> apiService.getTeraBoxDownloader(url).mapToEntity() ?: emptyList()
            THREADS -> apiService.getThreadsDownloader(url).mapToEntity() ?: emptyList()
            TIKTOK -> apiService.getTiktokDownloader(url).mapToEntity()
            TWITTER -> apiService.getTwitterDownloader(url).mapToEntity() ?: emptyList()
            VIDEY -> apiService.getVideyDownloader(url).mapToEntity()
            YOUTUBE, YOUTUBE_MUSIC -> apiService.getYoutubeDownloader(url, quality, type)
                .mapToEntity()

            else -> emptyList()
        }
}