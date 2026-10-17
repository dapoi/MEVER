package com.dapascript.mever.core.data.model.local

data class ArtEntity(
    val id: String,
    val title: String,
    val artistDisplay: String,
    val dateDisplay: String,
    val mediumDisplay: String,
    val placeOfOrigin: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val description: String
)