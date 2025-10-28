package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TiktokDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        @param:Json(name = "photo") val rawPhoto: Any?,
        @param:Json(name = "video") val rawVideo: Any?
    ) {
        val photos: List<String>?
            get() = if (rawPhoto is List<*>) {
                rawPhoto.mapNotNull { it as? String }
            } else null

        val video: String?
            get() = rawVideo as? String
    }

    fun mapToEntity(): List<ContentEntity> {
        val contentList = mutableListOf<ContentEntity>()
        data?.photos?.forEach { photoUrl ->
            contentList.add(
                ContentEntity(
                    url = photoUrl,
                    status = status ?: true,
                    type = "jpg"
                )
            )
        }
        data?.video?.let { videoUrl ->
            contentList.add(
                ContentEntity(
                    url = videoUrl,
                    status = status ?: true,
                    type = "mp4"
                )
            )
        }
        return contentList
    }
}