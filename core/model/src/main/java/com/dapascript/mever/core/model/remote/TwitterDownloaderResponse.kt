package com.dapascript.mever.core.model.remote

import com.dapascript.mever.core.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class TwitterDownloaderResponse(
    @SerializedName("media") val media: List<Any>
) {
    data class DataVideo(
        @SerializedName("quality") val quality: String? = null,
        @SerializedName("url") val url: String? = null
    )

    fun mapToEntity() = media.mapNotNull {
        when (it) {
            is DataVideo -> ContentEntity(
                url = it.url.orEmpty(),
                quality = it.quality.orEmpty()
            )

            is String -> ContentEntity(
                url = it,
                quality = ""
            )

            else -> null
        }
    }
}