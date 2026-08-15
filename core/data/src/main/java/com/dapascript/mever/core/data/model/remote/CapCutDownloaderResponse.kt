package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CapCutDownloaderResponse(
    val status: Boolean? = null,
    val data: DataContent? = null
) {
    @JsonClass(generateAdapter = true)
    data class DataContent(val url: String? = null)

    fun mapToEntity(): List<ContentEntity> {
        val contentList = mutableListOf<ContentEntity>()
        data?.url?.let { videoUrl ->
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