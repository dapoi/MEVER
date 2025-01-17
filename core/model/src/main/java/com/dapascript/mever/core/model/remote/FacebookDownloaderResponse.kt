package com.dapascript.mever.core.model.remote

import com.dapascript.mever.core.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class FacebookDownloaderResponse(
    @SerializedName("data") val data: List<DataVideo>
) {
    data class DataVideo(
        @SerializedName("resolution") val resolution: String? = null,
        @SerializedName("url") val url: String? = null
    )

    fun mapToEntity() = data.map {
        ContentEntity(
            url = it.url.orEmpty(),
            quality = it.resolution.orEmpty()
        )
    }
}