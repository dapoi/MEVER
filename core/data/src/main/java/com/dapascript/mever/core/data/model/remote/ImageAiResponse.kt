package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageAiResponse(val data: ImageAiData) {
    @JsonClass(generateAdapter = true)
    data class ImageAiData(
        val prompt: String?,
        val media: List<ImageAiMedia>? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class ImageAiMedia(val image: Image) {
            @JsonClass(generateAdapter = true)
            data class Image(val uri: String?)
        }
    }

    fun mapToEntity() = ImageAiEntity(
        prompt = data.prompt.orEmpty(),
        imagesUrl = data.media?.map { it.image.uri.orEmpty() } ?: emptyList()
    )
}