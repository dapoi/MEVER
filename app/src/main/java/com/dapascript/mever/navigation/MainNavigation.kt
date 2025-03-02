package com.dapascript.mever.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dapascript.mever.core.navigation.graph.MeverNavGraphs
import com.dapascript.mever.core.navigation.graph.route.StartupScreenRoute.SplashRoute

@Composable
internal fun MainNavigation(
    meverNavGraphs: MeverNavGraphs,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navGraphs = remember { meverNavGraphs.getNavGraphs() }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashRoute::class
    ) { navGraphs.forEach { navGraph -> navGraph.createGraph(navController, this) } }
}