package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface HomeScreenRoute {
    @Serializable
    data object HomeLandingRoute : HomeScreenRoute
}