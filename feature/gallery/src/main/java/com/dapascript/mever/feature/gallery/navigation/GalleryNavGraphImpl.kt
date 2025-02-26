package com.dapascript.mever.feature.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.extension.composableScreen
import com.dapascript.mever.core.navigation.graph.route.GraphRoute.GalleryNavGraphRoute
import com.dapascript.mever.core.navigation.graph.screen.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.graph.screen.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.feature.gallery.screen.GalleryContentDetailScreen
import com.dapascript.mever.feature.gallery.screen.GalleryLandingScreen
import javax.inject.Inject

class GalleryNavGraphImpl @Inject constructor() : BaseNavGraph() {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<GalleryNavGraphRoute>(startDestination = GalleryLandingRoute) {
            composableScreen<GalleryLandingRoute> { GalleryLandingScreen(navController) }
            composableScreen<GalleryContentDetailRoute> { GalleryContentDetailScreen(navController) }
        }
    }
}