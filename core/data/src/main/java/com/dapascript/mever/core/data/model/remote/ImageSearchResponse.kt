package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageSearchResponse(
    val status: Boolean? = null,
    val data: List<ImageSearchData>? = null
) {
    @JsonClass(generateAdapter = true)
    data class ImageSearchData(
        val title: String? = null,
        val content: List<ContentData>? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class ContentData(val url: String? = null)
    }

    fun mapToEntity() = data?.mapIndexed { index, content ->
        val url = content.content?.firstOrNull()?.url.orEmpty()
        ContentEntity(
            id = index.toString(),
            status = status ?: true,
            url = url,
            thumbnail = url.replace("/original/", "/236x/"),
            fileName = content.title.orEmpty()
        )
    }
}