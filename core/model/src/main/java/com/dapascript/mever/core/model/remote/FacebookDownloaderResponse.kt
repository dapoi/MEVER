package com.dapascript.mever.core.model.remote

import com.dapascript.mever.core.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class FacebookDownloaderResponse(
    @SerializedName("data") val data: List<DataVideo>? = null
) {
    data class DataVideo(
        @SerializedName("url") val url: String? = null,
        @SerializedName("resolution") val resolution: String? = null,
        @SerializedName("thumbnail") val thumbnail: String? = null
    )

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            quality = it.resolution.orEmpty(),
            thumbnail = it.thumbnail.orEmpty()
        )
    }
}