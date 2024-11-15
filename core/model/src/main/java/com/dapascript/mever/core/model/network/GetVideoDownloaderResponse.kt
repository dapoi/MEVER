package com.dapascript.mever.core.model.network

import com.dapascript.mever.core.model.local.VideoUrlEntity
import com.google.gson.annotations.SerializedName

data class GetVideoDownloaderResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("quality") val quality: List<VideoUrlEntity>
) {
    fun mapToEntity() = quality.map {
        VideoUrlEntity(
            quality = it.quality,
            url = it.url
        )
    }
}