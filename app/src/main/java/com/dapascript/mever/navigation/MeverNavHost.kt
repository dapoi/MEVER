package com.dapascript.mever.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.dapascript.mever.core.common.navigation.MeverNavGraphs
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.StartupNavGraphRoute
import kotlin.reflect.KClass

@Composable
fun MeverNavHost(
    meverNavGraphs: MeverNavGraphs,
    modifier: Modifier = Modifier,
    startDestination: KClass<*> = StartupNavGraphRoute::class
) {
    val navController = rememberNavController()
    val navGraphs = remember { meverNavGraphs.getNavGraphs() }
    val navigator = remember { BaseNavigator(navController, navGraphs) }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        navGraphs.forEach { navGraph -> navGraph.buildGraph(navigator, this) }
    }
}