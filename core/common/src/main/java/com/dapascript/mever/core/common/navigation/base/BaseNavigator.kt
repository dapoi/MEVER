package com.dapascript.mever.core.common.navigation.base

import androidx.navigation.NavHostController
import androidx.navigation.toRoute

class BaseNavigator(
    val navController: NavHostController,
    val navGraphs: List<BaseNavGraph>
) {
    fun navigate(
        route: Any,
        popUpTo: Any? = null,
        inclusive: Boolean = false,
        launchSingleTop: Boolean = false
    ) {
        navController.navigate(route) {
            popUpTo?.let {
                this.popUpTo(it) {
                    this.inclusive = inclusive
                }
            }

            this.launchSingleTop = launchSingleTop
        }
    }

    fun popBackStack() = navController.popBackStack()

    inline fun <reified T> getCurrentRoute() = navController.currentBackStackEntry?.toRoute<T>()

    inline fun <reified NavGraph : BaseNavGraph> getNavGraph() = navGraphs.find { it is NavGraph } as NavGraph
}