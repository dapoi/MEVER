package com.dapascript.mever.feature.gallery.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed class GalleryRoutes {
    @Serializable
    data object GalleryLandingRoute : GalleryRoutes()

    @Serializable
    data class GalleryContentDetailRoute(
        val id: Int,
        val sourceFile: String,
        val fileName: String
    ) : GalleryRoutes()
}