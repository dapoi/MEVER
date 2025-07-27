package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.google.gson.annotations.SerializedName

data class ImageAiResponse(@SerializedName("data") val data: ImageAiData) {
    data class ImageAiData(
        @SerializedName("prompt") val prompt: String,
        @SerializedName("url") val url: String
    )

    fun mapToEntity() = ImageAiEntity(
        prompt = data.prompt,
        imagesUrl = listOf(data.url)
    )
}