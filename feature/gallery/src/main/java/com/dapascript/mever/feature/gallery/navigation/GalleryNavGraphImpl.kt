package com.dapascript.mever.feature.gallery.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.graph.route.GalleryScreenRoute.GalleryContentDetailRoute
import com.dapascript.mever.core.navigation.graph.route.GalleryScreenRoute.GalleryLandingRoute
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.feature.gallery.screen.GalleryContentDetailScreen
import com.dapascript.mever.feature.gallery.screen.GalleryLandingScreen
import javax.inject.Inject

class GalleryNavGraphImpl @Inject constructor() : BaseNavGraph() {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = with(navGraphBuilder) {
        composableScreen<GalleryLandingRoute> { GalleryLandingScreen(navController) }
        composableScreen<GalleryContentDetailRoute> { GalleryContentDetailScreen(navController) }
    }
}