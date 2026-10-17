package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ArtEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ArtResponse(
    @Json(name = "data") val data: List<ArtDataResponse>? = null,
    @Json(name = "config") val config: ArtConfigResponse? = null
) {
    @JsonClass(generateAdapter = true)
    internal data class ArtConfigResponse(
        @Json(name = "iiif_url") val iiifUrl: String? = null
    )

    @JsonClass(generateAdapter = true)
    internal data class ArtDataResponse(
        @Json(name = "id") val id: Long? = null,
        @Json(name = "title") val title: String? = null,
        @Json(name = "artist_display") val artistDisplay: String? = null,
        @Json(name = "date_display") val dateDisplay: String? = null,
        @Json(name = "medium_display") val mediumDisplay: String? = null,
        @Json(name = "place_of_origin") val placeOfOrigin: String? = null,
        @Json(name = "description") val description: String? = null,
        @Json(name = "image_id") val imageId: String? = null
    )

    fun mapToEntity(): List<ArtEntity> {
        val iiifBaseUrl = config?.iiifUrl?.takeIf { it.isNotBlank() } ?: "https://www.artic.edu/iiif/2"
        return data?.mapNotNull { item ->
            val imageId = item.imageId?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            ArtEntity(
                id = item.id?.toString().orEmpty(),
                title = item.title.orEmpty().ifBlank { "Untitled Artwork" },
                artistDisplay = item.artistDisplay.orEmpty().ifBlank { "Unknown Artist" },
                dateDisplay = item.dateDisplay.orEmpty(),
                mediumDisplay = item.mediumDisplay.orEmpty(),
                placeOfOrigin = item.placeOfOrigin.orEmpty(),
                imageUrl = "$iiifBaseUrl/$imageId/full/843,/0/default.jpg",
                thumbnailUrl = "$iiifBaseUrl/$imageId/full/400,/0/default.jpg",
                description = item.description.orEmpty()
            )
        }.orEmpty()
    }
}
