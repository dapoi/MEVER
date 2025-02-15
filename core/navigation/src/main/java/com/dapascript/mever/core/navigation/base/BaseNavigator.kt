package com.dapascript.mever.core.navigation.base

import androidx.navigation.NavHostController

class BaseNavigator(
    private val navController: NavHostController,
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

    inline fun <reified NavGraph : BaseNavGraph> getNavGraph() = navGraphs.find { it is NavGraph } as NavGraph
}