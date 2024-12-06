package com.dapascript.mever.core.model.remote

import com.dapascript.mever.core.model.local.VideoGeneralEntity
import com.google.gson.annotations.SerializedName

data class TikTokDownloaderResponse(@SerializedName("data") val data: DataVideo) {
    data class DataVideo(@SerializedName("video_data") val videoData: VideoData) {
        data class VideoData(@SerializedName("wm_video_url") val videoUrl: String? = null)
    }

    fun mapToEntity() = listOf(
        VideoGeneralEntity(
            url = data.videoData.videoUrl.orEmpty(),
            quality = ""
        )
    )
}
