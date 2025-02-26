package com.dapascript.mever.feature.startup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.graph.route.GraphRoute.StartupNavGraphRoute
import com.dapascript.mever.core.navigation.graph.route.screen.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.core.navigation.graph.route.screen.StartupScreenRoute.SplashRoute
import com.dapascript.mever.feature.startup.screen.OnboardScreen
import com.dapascript.mever.feature.startup.screen.SplashScreen
import javax.inject.Inject

class StartupNavGraphImpl @Inject constructor() : BaseNavGraph() {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<StartupNavGraphRoute>(startDestination = SplashRoute) {
            composableScreen<SplashRoute> { SplashScreen(navController) }
            composableScreen<OnboardRoute> { OnboardScreen(navController) }
        }
    }
}