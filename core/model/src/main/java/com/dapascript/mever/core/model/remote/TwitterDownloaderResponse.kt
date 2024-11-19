package com.dapascript.mever.core.model.remote

import com.dapascript.mever.core.model.local.VideoGeneralEntity
import com.google.gson.annotations.SerializedName

data class TwitterDownloaderResponse(
    @SerializedName("type") val type: String? = null,
    @SerializedName("data") val data: List<DataVideo>
) {
    data class DataVideo(
        @SerializedName("quality") val quality: String? = null,
        @SerializedName("url") val url: String? = null
    )

    fun mapToEntity() = data.map {
        VideoGeneralEntity(
            url = it.url.orEmpty(),
            quality = it.quality.orEmpty()
        )
    }
}