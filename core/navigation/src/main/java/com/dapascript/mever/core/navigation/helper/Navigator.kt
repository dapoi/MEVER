package com.dapascript.mever.core.navigation.helper

import android.app.Activity
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlin.reflect.KClass

class Navigator(
    val state: NavigationState,
    private val activity: Activity? = null
) {
    private val _navResult = Channel<Any>(capacity = 1, onBufferOverflow = DROP_OLDEST)
    val navResult = _navResult.receiveAsFlow()

    private var lastBackPressTime = 0L
    private val backPressThreshold = 300L

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

    fun navigateBack(
        isInclusive: Boolean = false,
        route: Any? = null,
        result: Any? = null
    ) {
        result?.let { _navResult.trySend(it) }
        route?.let {
            state.backStacks.values.forEach { stack ->
                val index = when (route) {
                    is NavKey -> stack.indexOf(route)
                    is KClass<*> -> stack.indexOfFirst { route.isInstance(it) }
                    else -> -1
                }
                if (index != -1) {
                    val removeCount = if (isInclusive) stack.size - index else stack.size - index - 1
                    repeat(removeCount) { stack.removeLastOrNull() }
                }
            }
        } ?: navigateBack()
    }

    fun navigateBack() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < backPressThreshold) return

        lastBackPressTime = currentTime

        val currentBackStack = state.backStacks[state.topLevelRoute] ?: return
        val currentRoute = currentBackStack.last()

        when (currentRoute) {
            is SplashRoute -> activity?.finish()
            state.topLevelRoute -> state.topLevelRoute = state.startRoute
            else -> currentBackStack.removeLastOrNull()
        }
    }
}