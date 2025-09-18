package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageSearchResponse(
    val status: Boolean,
    val data: List<ImageSearchData>? = null
) {
    @JsonClass(generateAdapter = true)
    data class ImageSearchData(
        val id: String? = null,
        val url: String? = null,
        val preview: Preview? = null,
        val origin: ImageOrigin? = null
    ) {
        @JsonClass(generateAdapter = true)
        data class Preview(val url: String? = null)

        @JsonClass(generateAdapter = true)
        data class ImageOrigin(val title: String? = null)
    }

    fun mapToEntity() = data?.map {
        ContentEntity(
            url = it.url.orEmpty(),
            previewUrl = it.preview?.url.orEmpty(),
            id = it.id.orEmpty(),
            status = status,
            fileName = it.origin?.title.orEmpty()
        )
    }
}