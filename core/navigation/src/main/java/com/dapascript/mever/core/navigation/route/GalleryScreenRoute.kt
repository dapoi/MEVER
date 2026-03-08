package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface GalleryScreenRoute {
    @Serializable
    data object GalleryLandingRoute : GalleryScreenRoute

    @Serializable
    data class GalleryContentDetailRoute(
        val contents: List<Content>,
        val initialIndex: Int
    ) : GalleryScreenRoute {
        @Serializable
        data class Content(
            val id: Int,
            val isVideo: Boolean,
            val fileName: String,
            val primaryContent: String,
            val isDownloadable: Boolean = false,
            val isPreview: Boolean = false
        )
    }
}