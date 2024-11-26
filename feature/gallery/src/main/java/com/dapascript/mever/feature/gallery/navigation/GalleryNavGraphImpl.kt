package com.dapascript.mever.feature.gallery.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.extension.composableScreen
import com.dapascript.mever.core.common.navigation.graph.GalleryNavGraph
import com.dapascript.mever.core.common.navigation.graph.GalleryNavGraphRoute
import com.dapascript.mever.feature.gallery.navigation.route.GalleryLandingRoute
import com.dapascript.mever.feature.gallery.screen.GalleryScreen
import javax.inject.Inject

class GalleryNavGraphImpl @Inject constructor() : GalleryNavGraph() {
    override fun buildGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<GalleryNavGraphRoute>(startDestination = GalleryLandingRoute) {
            composableScreen<GalleryLandingRoute> { GalleryScreen(navigator) }
        }
    }

    override fun getGalleryRoute() = GalleryLandingRoute
}