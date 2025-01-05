package com.dapascript.mever.feature.gallery.navigation.route

import kotlinx.serialization.Serializable

@Serializable
data class GalleryContentViewerRoute(
    val id: Int,
    val sourceFile: String,
    val fileName: String
)