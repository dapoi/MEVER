package com.dapascript.mever.core.common.navigation.graph.route

import kotlinx.serialization.Serializable

@Serializable
sealed class GraphRoute {
    @Serializable
    data object HomeNavGraphRoute : GraphRoute()

    @Serializable
    data object GalleryNavGraphRoute : GraphRoute()

    @Serializable
    data object SettingNavGraphRoute : GraphRoute()

    @Serializable
    data object StartupNavGraphRoute : GraphRoute()
}