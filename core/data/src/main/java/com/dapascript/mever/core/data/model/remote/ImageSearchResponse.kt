package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ImageSearchResponse(
    val status: Boolean? = null,
    val data: List<ImageSearchData>? = null
) {
    @JsonClass(generateAdapter = true)
    internal data class ImageSearchData(val content: List<ContentData>? = null) {
        @JsonClass(generateAdapter = true)
        internal data class ContentData(val url: String? = null)
    }

    fun mapToEntity() = data?.mapIndexed { index, content ->
        val url = content.content?.firstOrNull()?.url.orEmpty()
        ContentEntity(
            id = index.toString(),
            status = status ?: true,
            url = url,
            thumbnail = url.replace("/original/", "/236x/")
        )
    }
}