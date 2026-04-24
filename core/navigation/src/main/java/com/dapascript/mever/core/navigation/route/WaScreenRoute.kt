package com.dapascript.mever.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface WaScreenRoute {
    @Serializable
    data object WaStatusRoute : WaScreenRoute
}