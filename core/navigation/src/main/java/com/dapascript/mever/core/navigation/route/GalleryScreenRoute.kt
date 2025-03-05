package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed class GalleryScreenRoute {
    @Serializable
    data object GalleryLandingRoute : GalleryScreenRoute()

    @Serializable
    data class GalleryContentDetailRoute(
        val id: Int,
        val sourceFile: String,
        val fileName: String
    ) : GalleryScreenRoute()
}