package com.dapascript.mever.feature.startup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.graph.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.core.navigation.graph.route.StartupScreenRoute.SplashRoute
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.feature.startup.screen.OnboardScreen
import com.dapascript.mever.feature.startup.screen.SplashScreen
import javax.inject.Inject

class StartupNavGraphImpl @Inject constructor() : BaseNavGraph() {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = with(navGraphBuilder) {
        composableScreen<SplashRoute> { SplashScreen(navController) }
        composableScreen<OnboardRoute> { OnboardScreen(navController) }
    }
}