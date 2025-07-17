package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class YouTubeDownloaderResponse(
    @SerializedName("data") val data: DataContent,
    @SerializedName("thumbnail") val thumbnail: String? = null
) {
    data class DataContent(
        @SerializedName("url") val url: String? = null,
        @SerializedName("quality") val quality: String? = null
    )

    fun mapToEntity() = listOf(
        ContentEntity(
            url = data.url.orEmpty(),
            quality = data.quality.orEmpty(),
            thumbnail = thumbnail.orEmpty()
        )
    )
}
