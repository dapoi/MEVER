package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface HomeScreenRoute {
    @Serializable
    data object HomeLandingRoute : HomeScreenRoute

    @Serializable
    data class HomeImageGeneratorResultRoute(
        val prompt: String,
        val artStyle: String,
        val totalImages: Int
    ) : HomeScreenRoute
}