package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PinterestDownloaderResponse(
    val data: DataContent
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        @param:Json(name = "is_video") val isVideo: Boolean? = null,
        @param:Json(name = "content") val contents: List<Contents>? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Contents(
            val url: String? = null,
            val thumbnail: String? = null
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
