package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TiktokDownloaderResponse(
    val data: DataContent
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(
        @Json(name = "photo") val rawPhoto: Any?,
        @Json(name = "video") val rawVideo: Any?
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