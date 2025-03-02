package com.dapascript.mever.core.navigation.graph.route

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreenRoute {
    @Serializable
    data object HomeLandingRoute : HomeScreenRoute()
}