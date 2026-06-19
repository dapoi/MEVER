package com.dapascript.mever.core.navigation.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface StartupScreenRoute : NavKey {
    @Serializable
    data object SplashRoute : StartupScreenRoute

    @Serializable
    data object OnboardRoute : StartupScreenRoute
}