package com.dapascript.mever.core.navigation.base

import androidx.navigation.NavHostController
import kotlinx.serialization.json.Json

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

    inline fun <reified T> setPopBackStackWithCustomArgs(
        key: String,
        value: T
    ) = navController.previousBackStackEntry?.savedStateHandle?.set(key, Json.encodeToString(value))

    inline fun <reified T> getPopBackStackWithCustomArgs(
        key: String
    ): T? = try {
        navController.currentBackStackEntry?.savedStateHandle?.run {
            val result = get<String>(key).orEmpty()
            remove<String>(key)
            Json.decodeFromString(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    inline fun <reified NavGraph : BaseNavGraph> getNavGraph() = navGraphs.find { it is NavGraph } as NavGraph
}