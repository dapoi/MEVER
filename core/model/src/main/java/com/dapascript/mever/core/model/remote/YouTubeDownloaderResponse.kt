package com.dapascript.mever.core.model.remote

import com.dapascript.mever.core.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class YouTubeDownloaderResponse(
    @SerializedName("url") val url: String? = null,
    @SerializedName("thumbnail") val thumbnailUrl: String? = null
) {
    fun mapToEntity() = listOf(
        ContentEntity(
            url = url.orEmpty(),
            quality = "",
            thumbnail = thumbnailUrl.orEmpty()
        )
    )
}
