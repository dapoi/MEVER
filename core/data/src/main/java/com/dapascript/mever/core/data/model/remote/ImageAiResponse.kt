package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageAiResponse(val data: ImageAiData) {
    @JsonClass(generateAdapter = true)
    data class ImageAiData(
        val media: List<ImageAiMedia>? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class ImageAiMedia(val url: String? = null)
    }

    fun mapToEntity() = ImageAiEntity(data.media?.map { it.url.orEmpty() }.orEmpty())
}