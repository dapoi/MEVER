package com.dapascript.mever.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute

@Composable
internal fun MainNavigation(
    navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashRoute::class
    ) { navGraphs.forEach { navGraph -> navGraph.createGraph(navController, this) } }
}