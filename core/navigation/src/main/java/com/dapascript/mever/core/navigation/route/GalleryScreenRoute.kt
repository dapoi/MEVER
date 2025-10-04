package com.dapascript.mever.core.navigation.route

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.dapascript.mever.core.navigation.helper.generateCustomNavType
import kotlinx.serialization.Serializable

@Serializable
sealed class GalleryScreenRoute {
    @Serializable
    data object GalleryLandingRoute : GalleryScreenRoute()

    @Serializable
    data class GalleryContentDetailRoute(
        val contents: List<Content>,
        val initialIndex: Int
    ) : GalleryScreenRoute() {
        @Serializable
        data class Content(
            val id: Int,
            val isVideo: Boolean,
            val isOnlineContent: Boolean = false,
            val fileName: String = "",
            val primaryContent: String = "",
            val secondaryContent: String = ""
        ) : GalleryScreenRoute()

        companion object {
            val typeMap = mapOf(generateCustomNavType<List<Content>>())
            fun getArgs(
                savedStateHandle: SavedStateHandle
            ) = savedStateHandle.toRoute<GalleryContentDetailRoute>(typeMap)
        }
    }
}