package com.dapascript.mever.core.data.worker

import android.content.Context
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
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Path.HOME
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Path.IMAGE_GENERATOR
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.ART_STYLE
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.ERROR
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.PROMPT
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.RESPONSES
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.URL
import com.dapascript.mever.core.common.util.deeplink.DeeplinkNotificationManager.showNotification
import com.dapascript.mever.core.common.util.deeplink.buildMeverDeeplink
import com.dapascript.mever.core.common.util.encodeMetaData
import com.dapascript.mever.core.common.util.getExtensionFromUrl
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.sanitizeFilename
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_DOWNLOAD
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_GENERATE_AI
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ACTION
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ART_STYLE
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_IS_FROM_NOTIF
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
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import com.ketch.Ketch
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
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.showQuickDownloadNotification as showQuickDownloadNotificationHelper

@HiltWorker
internal class MeverWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val apiService: ApiService,
    private val moshiHelper: MoshiHelper,
    private val ketch: Ketch
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val action = inputData.getString(KEY_ACTION)
        val url = inputData.getString(KEY_URL).orEmpty()
        val quality = inputData.getString(KEY_QUALITY).orEmpty()
        val type = inputData.getString(KEY_TYPE) ?: "video"
        val prompt = inputData.getString(KEY_PROMPT).orEmpty()
        val isFromNotif = inputData.getBoolean(KEY_IS_FROM_NOTIF, false)
        val platform = getPlatformType(url, type)
        val effectiveQuality = if (quality.isEmpty() && (platform == YOUTUBE || platform == YOUTUBE_MUSIC)) {
            if (type == "audio") "128kbps" else "480p"
        } else {
            quality
        }

        if (isFromNotif) {
            showQuickDownloadNotificationHelper(
                context = context,
                isProcessing = true,
                processingUrl = url
            )
        }

        return try {
            val resultData = when (action) {
                ACTION_DOWNLOAD -> {
                    currentCoroutineContext().ensureActive()
                    val res = getApiDownloader(
                        url = url,
                        quality = effectiveQuality,
                        type = type
                    )

                    if (res.firstOrNull()?.status != true) {
                        throw Exception(context.getString(R.string.url_error))
                    }

                    if (isFromNotif) {
                        val bestContents = selectBestContents(res, url)
                        val meverFolder = getMeverFolder()
                        val platformTag = getPlatformType(url, type).platformName

                        bestContents.forEachIndexed { index, content ->
                            val extension = getExtensionFromUrl(
                                url = content.downloadUrl,
                                extensionFromResponse = content.type
                            ) ?: run {
                                val contentType = content.type.lowercase()
                                when {
                                    contentType.contains("video") || content.downloadUrl.contains("mp4") -> ".mp4"
                                    contentType.contains("image") || content.downloadUrl.contains("jpg") || content.downloadUrl.contains(
                                        "jpeg"
                                    ) -> ".jpg"

                                    contentType.contains("audio") || content.downloadUrl.contains("mp3") -> ".mp3"
                                    else -> ".jpg"
                                }
                            }

                            val timeStamp = changeToCurrentDate(System.currentTimeMillis())
                            val rawFileName = content.fileName.ifEmpty {
                                "MEVER_${timeStamp}_${index}${extension}"
                            }

                            val sanitized = sanitizeFilename(rawFileName)

                            ketch.download(
                                url = content.downloadUrl,
                                path = meverFolder.path,
                                fileName = sanitized,
                                tag = platformTag,
                                metaData = encodeMetaData(content.thumbnail, rawFileName)
                            )
                        }
                        bestContents
                    } else {
                        res
                    }
                }

                ACTION_GENERATE_AI -> {
                    currentCoroutineContext().ensureActive()
                    val artStyle = inputData.getString(KEY_ART_STYLE).orEmpty()
                    val promptWithStyle = if (artStyle.isNotEmpty()) {
                        "Generate an image of $prompt in $artStyle style"
                    } else prompt

                    apiService.getImageAiGenerator(promptWithStyle).mapToEntity()
                }

                else -> throw IllegalArgumentException("Unknown action: $action")
            }

            val jsonOutput = moshiHelper.toJson(resultData)
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
            val deeplinkDownload = buildMeverDeeplink(
                path = HOME,
                params = mapOf(
                    URL to url,
                    RESPONSES to jsonOutput
                )
            )
            val deeplinkImageGenerator = buildMeverDeeplink(
                path = IMAGE_GENERATOR,
                params = mapOf(
                    PROMPT to prompt,
                    ART_STYLE to inputData.getString(KEY_ART_STYLE).orEmpty(),
                    RESPONSES to jsonOutput
                )
            )
            val notifData = Triple(
                if (action == ACTION_DOWNLOAD) context.getString(
                    R.string.notif_link_found_title,
                    getPlatformType(url, type).platformName
                ) else context.getString(R.string.notif_ai_title),
                context.getString(
                    if (action == ACTION_DOWNLOAD) R.string.notif_link_found_description
                    else R.string.notif_ai_desc
                ),
                if (action == ACTION_DOWNLOAD) deeplinkDownload else deeplinkImageGenerator
            )

            if (isFromNotif) {
                showQuickDownloadNotificationHelper(
                    context = context,
                    isProcessing = false,
                    processingUrl = url
                )
            } else {
                notifData.let { (title, desc, deeplink) ->
                    showNotification(
                        context = context,
                        title = title,
                        desc = desc,
                        deeplink = deeplink
                    )
                }
            }
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
            val deeplinkDownload = buildMeverDeeplink(
                path = HOME,
                params = mapOf(
                    URL to url,
                    ERROR to errorMessage
                )
            )
            val deeplinkImageGenerator = buildMeverDeeplink(
                path = IMAGE_GENERATOR,
                params = mapOf(
                    PROMPT to prompt,
                    ART_STYLE to inputData.getString(KEY_ART_STYLE).orEmpty(),
                    ERROR to errorMessage
                )
            )
            val deeplink =
                if (action == ACTION_DOWNLOAD) deeplinkDownload else deeplinkImageGenerator
            if (isFromNotif) {
                showQuickDownloadNotificationHelper(
                    context = context,
                    isProcessing = false,
                    processingUrl = url
                )
            }
            showNotification(
                context = context,
                title = context.getString(UiR.string.error_title),
                desc = errorMessage,
                deeplink = deeplink
            )
            Result.failure(workDataOf(KEY_ERROR to errorMessage))
        }
    }

    private fun selectBestContents(
        contents: List<ContentEntity>,
        url: String,
    ): List<ContentEntity> {
        if (contents.isEmpty()) return emptyList()

        return when (getPlatformType(url)) {
            YOUTUBE -> {
                val yt480 = contents.firstOrNull { (it.quality).contains("480p", ignoreCase = true) }
                    ?: contents.firstOrNull { (it.quality).contains("720p", ignoreCase = true) }
                    ?: contents.firstOrNull { (it.quality).contains("360p", ignoreCase = true) }
                    ?: contents.first()
                listOf(yt480)
            }

            FACEBOOK -> {
                val fbHd = contents.firstOrNull { (it.quality).contains("hd", ignoreCase = true) }
                    ?: contents.first()
                listOf(fbHd)
            }

            TIKTOK, INSTAGRAM, THREADS, X, PINTEREST, PIXIV, TERABOX, CAPCUT, DOUYIN, VIDEY -> {
                contents
            }

            else -> contents
        }
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
}