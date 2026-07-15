package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface GalleryScreenRoute : NavKey {
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
            val media: String,
            val isDownloadable: Boolean = false,
            val isPreview: Boolean = false,
            val isDeletable: Boolean = true
        )
    }
}