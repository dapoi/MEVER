package com.dapascript.mever.core.navigation.graph.screen

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreenRoute {
    @Serializable
    data object HomeLandingRoute : HomeScreenRoute()
}