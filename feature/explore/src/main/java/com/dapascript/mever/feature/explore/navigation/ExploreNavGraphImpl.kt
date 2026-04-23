package com.dapascript.mever.feature.explore.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.route.ExploreScreenRoute.ExploreLandingRoute
import com.dapascript.mever.feature.explore.screen.ExploreLandingScreen
import javax.inject.Inject

class ExploreNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = navGraphBuilder.composableScreen<ExploreLandingRoute> {
        ExploreLandingScreen(navController)
    }
}