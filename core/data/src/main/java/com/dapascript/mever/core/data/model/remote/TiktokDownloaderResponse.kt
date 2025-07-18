package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class TiktokDownloaderResponse(
    @SerializedName("data") val data: DataContent
) {
    data class DataContent(
        @SerializedName("photo") val rawPhoto: JsonElement?,
        @SerializedName("video") val rawVideo: JsonElement?
    ) {
        val photos: List<String>?
            get() = if (rawPhoto?.isJsonArray == true) {
                rawPhoto.asJsonArray.mapNotNull {
                    if (it.isJsonPrimitive && it.asJsonPrimitive.isString) it.asString else null
                }
            } else null
        val video: String?
            get() = if (rawVideo?.isJsonPrimitive == true && rawVideo.asJsonPrimitive.isString) {
                rawVideo.asString
            } else null
    }

    fun mapToEntity(): List<ContentEntity> {
        val contentList = mutableListOf<ContentEntity>()
        data.photos?.forEach { photoUrl ->
            contentList.add(
                ContentEntity(
                    url = photoUrl,
                    type = "jpg"
                )
            )
        }
        data.video?.let { videoUrl ->
            contentList.add(
                ContentEntity(
                    url = videoUrl,
                    type = "mp4"
                )
            )
        }
        return contentList
    }
}
