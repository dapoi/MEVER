package com.dapascript.mever.core.data.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_HIGH
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.PlatformType.APPLE_MUSIC
import com.dapascript.mever.core.common.util.PlatformType.CAPCUT
import com.dapascript.mever.core.common.util.PlatformType.DOUYIN
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.PIXIV
import com.dapascript.mever.core.common.util.PlatformType.SOUNDCLOUD
import com.dapascript.mever.core.common.util.PlatformType.SPOTIFY
import com.dapascript.mever.core.common.util.PlatformType.TERABOX
import com.dapascript.mever.core.common.util.PlatformType.THREADS
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.VIDEY
import com.dapascript.mever.core.common.util.PlatformType.X
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.DEEPLINK_HOST
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.DEEPLINK_SCHEME
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.PATH_HOME
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.QUERY_RESPONSES
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.QUERY_URL
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import com.dapascript.mever.core.common.R as UiR

@HiltWorker
internal class MeverWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val moshiHelper: MoshiHelper
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = try {
        val action = inputData.getString(KEY_ACTION)
        val url = inputData.getString(KEY_URL).orEmpty()
        val quality = inputData.getString(KEY_QUALITY).orEmpty()
        val type = inputData.getString(KEY_TYPE) ?: "video"
        val prompt = inputData.getString(KEY_PROMPT).orEmpty()

        val (resultData, resultType) = when (action) {
            ACTION_DOWNLOAD -> {
                currentCoroutineContext().ensureActive()
                val res = getApiDownloader(
                    url = url,
                    quality = quality,
                    type = type
                )

                if (res.firstOrNull()?.status != true) {
                    throw Exception(context.getString(R.string.url_error))
                }

                res to Types.newParameterizedType(List::class.java, ContentEntity::class.java)
            }

            ACTION_GENERATE_AI -> {
                currentCoroutineContext().ensureActive()
                apiService.getImageAiGenerator(prompt).mapToEntity() to ImageAiEntity::class.java
            }

            else -> throw IllegalArgumentException("Unknown action: $action")
        }

        val jsonOutput = moshiHelper.toJson(resultType, resultData)
        val size = jsonOutput?.toByteArray()?.size ?: 0
        val outputData = if (size > SIZE_LIMIT) {
            val path = context.cacheDir
                .resolve("${KEY_RESULT}_${System.currentTimeMillis()}.json")
                .apply { writeText(jsonOutput!!) }.absolutePath
            workDataOf(
                KEY_OUTPUT_IS_FILE to true,
                KEY_OUTPUT_FILE_PATH to path
            )
        } else {
            workDataOf(
                KEY_OUTPUT_IS_FILE to false,
                KEY_RESULT to jsonOutput
            )
        }
        val encodedJson = jsonOutput?.let { Uri.encode(it) }.orEmpty()
        val deeplink =
            "$DEEPLINK_SCHEME://$DEEPLINK_HOST$PATH_HOME?$QUERY_URL=${Uri.encode(url)}&$QUERY_RESPONSES=$encodedJson"

        showNotification(
            title = context.getString(
                R.string.notif_link_found_title,
                getPlatformType(url, type).platformName
            ),
            desc = context.getString(R.string.notif_link_found_description),
            deeplink = deeplink
        )
        Result.success(outputData)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        val errorMessage = when (e) {
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is UnknownHostException -> context.getString(R.string.error_no_host)
            is IOException -> context.getString(R.string.error_io)
            is HttpException -> context.getString(R.string.error_http, e.code())
            else -> e.message ?: context.getString(R.string.error_unknown)
        }
        showNotification(
            title = context.getString(UiR.string.error_title),
            desc = errorMessage,
            deeplink = "$DEEPLINK_SCHEME://$DEEPLINK_HOST$PATH_HOME"
        )
        Result.failure(workDataOf(KEY_ERROR to errorMessage))
    }

    private suspend fun getApiDownloader(
        url: String,
        quality: String,
        type: String
    ) = when (getPlatformType(url, type)) {
        APPLE_MUSIC -> apiService.getAppleMusicDownloader(url).mapToEntity()
        CAPCUT -> apiService.getCapCutDownloader(url).mapToEntity()
        DOUYIN -> apiService.getDouyinDownloader(url).mapToEntity()
        FACEBOOK -> apiService.getFacebookDownloader(url).mapToEntity().orEmpty()
        INSTAGRAM -> apiService.getInstagramDownloader(url).mapToEntity().orEmpty()
        PINTEREST -> apiService.getPinterestDownloader(url).mapToEntity().orEmpty()
        PIXIV -> apiService.getPixivDownloader(url).mapToEntity().orEmpty()
        SOUNDCLOUD -> apiService.getSoundCloudDownloader(url).mapToEntity()
        SPOTIFY -> apiService.getSpotifyDownloader(url).mapToEntity()
        TERABOX -> apiService.getTeraBoxDownloader(url).mapToEntity().orEmpty()
        THREADS -> apiService.getThreadsDownloader(url).mapToEntity().orEmpty()
        TIKTOK -> apiService.getTiktokDownloader(url).mapToEntity()
        X -> apiService.getTwitterDownloader(url).mapToEntity().orEmpty()
        VIDEY -> apiService.getVideyDownloader(url).mapToEntity()
        YOUTUBE, YOUTUBE_MUSIC -> apiService.getYoutubeDownloader(url, quality, type).mapToEntity()
        else -> emptyList()
    }

    private fun showNotification(
        title: String,
        desc: String,
        deeplink: String
    ) {
        val notificationManager = context.getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_HIGH
        )
        val intent = Intent(
            ACTION_VIEW,
            deeplink.toUri(),
            context,
            Class.forName(TARGET_ACTIVITY_CLASS_NAME)
        ).apply { flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK }
        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(UiR.drawable.ic_mever)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val TARGET_ACTIVITY_CLASS_NAME = "com.dapascript.mever.screen.MainActivity"
        private const val NOTIFICATION_CHANNEL_NAME = "MEVER Fetch Result"
        private const val NOTIFICATION_CHANNEL_ID = "mever_result_channel"
        private const val NOTIFICATION_ID = 1001
    }
}