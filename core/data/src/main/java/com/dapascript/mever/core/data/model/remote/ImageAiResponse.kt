package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.google.gson.annotations.SerializedName

data class ImageAiResponse(@SerializedName("data") val data: ImageAiData) {
    data class ImageAiData(
        @SerializedName("prompt") val prompt: String,
        @SerializedName("media") val images: List<Media>
    ) {
        data class Media(@SerializedName("uri") val uri: String)
    }

    fun mapToEntity() = ImageAiEntity(
        prompt = data.prompt,
        imagesUrl = data.images.map { it.uri }
    )
}