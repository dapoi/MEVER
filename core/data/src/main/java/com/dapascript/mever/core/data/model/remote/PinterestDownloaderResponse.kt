package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.annotations.SerializedName

data class PinterestDownloaderResponse(
    @SerializedName("data") val data: DataContent
) {
    data class DataContent(
        @SerializedName("is_video") val isVideo: Boolean? = null,
        @SerializedName("content") val contents: List<Contents>? = null
    ) {
        data class Contents(
            @SerializedName("url") val url: String? = null,
            @SerializedName("thumbnail") val thumbnail: String? = null
        )
    }

    fun mapToEntity() = data.contents?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            thumbnail = it.thumbnail.orEmpty(),
            type = if (data.isVideo == true) "mp4" else "jpg"
        )
    }
}
