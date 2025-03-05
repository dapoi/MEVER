package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreenRoute {
    @Serializable
    data object HomeLandingRoute : HomeScreenRoute()
}