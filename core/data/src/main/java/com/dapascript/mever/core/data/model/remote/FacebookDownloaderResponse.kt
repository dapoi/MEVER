package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class FacebookDownloaderResponse(
    @SerializedName("data") val data: List<DataVideo>? = null
) {
    data class DataVideo(
        @SerializedName("url") val url: String? = null,
        @SerializedName("quality") val quality: String? = null
    )

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            quality = it.quality.orEmpty(),
            thumbnail = ""
        )
    }
}