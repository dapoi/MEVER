package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface StartupScreenRoute {
    @Serializable
    data object SplashRoute : StartupScreenRoute

    @Serializable
    data object OnboardRoute : StartupScreenRoute
}