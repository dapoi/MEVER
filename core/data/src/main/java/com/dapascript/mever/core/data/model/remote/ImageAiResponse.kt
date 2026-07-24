package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageAiResponse(val data: ImageAiData) {
    @JsonClass(generateAdapter = true)
    data class ImageAiData(val url: String? = null)

    fun mapToEntity() = ImageAiEntity(data.url.orEmpty())
}