package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface AiScreenRoute {
    @Serializable
    data class AiImageResultRoute(
        val prompt: String,
        val artStyle: String,
        val totalImages: Int
    ) : AiScreenRoute
}