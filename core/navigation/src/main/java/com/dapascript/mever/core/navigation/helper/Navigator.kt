package com.dapascript.mever.core.navigation.helper

import android.app.Activity
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.OnboardRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.reflect.KClass

class Navigator(
    val state: NavigationState,
    private val activity: Activity? = null
) {
    private val _results = MutableSharedFlow<Any>(extraBufferCapacity = 1)
    val results = _results.asSharedFlow()

    private var lastBackPressTime = 0L
    private val backPressThreshold = 500L

    fun navigate(
        route: NavKey,
        isInclusive: Boolean = false,
        isClearBackStacks: Boolean = false,
        popUpTo: Any? = null
    ) {
        if (isClearBackStacks) {
            state.backStacks.values.forEach { it.clear() }
        } else {
            popUpTo?.let { key ->
                state.backStacks.values.forEach { stack ->
                    val index = when (key) {
                        is NavKey -> stack.indexOf(key)
                        is KClass<*> -> stack.indexOfFirst { key.isInstance(it) }
                        else -> -1
                    }
                    if (index != -1) {
                        val removeCount = if (isInclusive) stack.size - index
                        else stack.size - index - 1
                        repeat(removeCount) { stack.removeLastOrNull() }
                    }
                }
            }
        }

        if (route in state.backStacks.keys) {
            state.topLevelRoute = route
        } else {
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    fun goBack(
        route: Any,
        inclusive: Boolean = false,
        result: Any? = null
    ) {
        result?.let { _results.tryEmit(it) }

        state.backStacks.values.forEach { stack ->
            val index = when (route) {
                is NavKey -> stack.indexOf(route)
                is KClass<*> -> stack.indexOfFirst { route.isInstance(it) }
                else -> -1
            }
            if (index != -1) {
                val removeCount = if (inclusive) stack.size - index else stack.size - index - 1
                repeat(removeCount) { stack.removeLastOrNull() }
            }
        }
    }

    fun goBack() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < backPressThreshold) return

        lastBackPressTime = currentTime

        val currentBackStack = state.backStacks[state.topLevelRoute] ?: return
        val currentRoute = currentBackStack.last()
        val isRootScreen = currentRoute is HomeLandingRoute ||
                currentRoute is OnboardRoute ||
                currentRoute is SplashRoute

        when {
            isRootScreen -> activity?.finish()
            currentRoute == state.topLevelRoute -> state.topLevelRoute = state.startRoute
            else -> currentBackStack.removeLastOrNull()
        }
    }
}