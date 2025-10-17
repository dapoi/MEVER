package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.DOUYIN
import com.dapascript.mever.core.common.util.PlatformType.EXPLORE
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.SOUNDCLOUD
import com.dapascript.mever.core.common.util.PlatformType.SPOTIFY
import com.dapascript.mever.core.common.util.PlatformType.TERABOX
import com.dapascript.mever.core.common.util.PlatformType.THREADS
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.PlatformType.VIDEY
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_SELECTED_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_CONTENTS
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_TYPE
import com.dapascript.mever.core.data.R
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.worker.base.BaseWorker
import com.squareup.moshi.Types
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.reflect.Type

@HiltWorker
class DownloaderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    moshiHelper: MoshiHelper,
    private val apiService: ApiService
) : BaseWorker<List<ContentEntity>>(context, workerParameters, moshiHelper) {
    override val outputSuccessKey: String = KEY_RESPONSE_CONTENTS
    override val resultType: Type = Types.newParameterizedType(
        List::class.java, ContentEntity::class.java
    )

    override suspend fun doApiCall(): List<ContentEntity> {
        val link = inputData.getString(KEY_REQUEST_URL).orEmpty()
        val selectedQuality = inputData.getString(KEY_REQUEST_SELECTED_QUALITY).orEmpty()
        val type = inputData.getString(KEY_RESPONSE_TYPE) ?: "video"
        return apiService.getApiDownloader(link, selectedQuality, type).let { contents ->
            contents?.firstOrNull()?.run {
                when {
                    status -> contents
                    message.contains("30") -> throw Throwable(
                        context.getString(R.string.max_duration_exceeded)
                    )
                    else -> emptyList()
                }
            } ?: emptyList()
        }
    }

    private suspend fun ApiService.getApiDownloader(
        url: String,
        selectedQuality: String,
        type: String
    ) = when (getPlatformType(url, type)) {
        DOUYIN -> getDouyinDownloader(url).mapToEntity()
        FACEBOOK -> getFacebookDownloader(url).mapToEntity()
        INSTAGRAM -> getInstagramDownloader(url).mapToEntity()
        PINTEREST -> getPinterestDownloader(url).mapToEntity()
        SOUNDCLOUD -> getSoundCloudDownloader(url).mapToEntity()
        SPOTIFY -> getSpotifyDownloader(url).mapToEntity()
        TERABOX -> getTeraBoxDownloader(url).mapToEntity()
        THREADS -> getThreadsDownloader(url).mapToEntity()
        TIKTOK -> getTiktokDownloader(url).mapToEntity()
        TWITTER -> getTwitterDownloader(url).mapToEntity()
        VIDEY -> getVideyDownloader(url).mapToEntity()
        YOUTUBE, YOUTUBE_MUSIC -> getYoutubeDownloader(url, selectedQuality, type).mapToEntity()
        AI, ALL, EXPLORE -> throw Throwable("Platform not supported")
    }
}