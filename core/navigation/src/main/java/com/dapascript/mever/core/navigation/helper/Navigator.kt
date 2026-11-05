package com.dapascript.mever.core.navigation.helper

import android.app.Activity
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
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

    /**
     * Navigates to a specific screen (route).
     *
     * @param route The destination screen to go to.
     * @param isInclusive If true, and popUpTo is set, it will remove the popUpTo screen as well.
     * @param isClearBackStacks If true, it wipes out all navigation history before going to this route.
     * @param popUpTo Can be either an exact screen instance (e.g., DetailRoute(1)) or a KClass (e.g., DetailRoute::class) to pop back to.
     */
    fun navigate(
        route: NavKey,
        isInclusive: Boolean = false,
        isClearBackStacks: Boolean = false,
        popUpTo: Any? = null
    ) {
        // A top-level route is one of our main tabs or starting points (like Home or Splash).
        // It has its own dedicated back stack (history of screens).
        val isTopLevelRoute = route in state.backStacks.keys

        // 1. Handle clearing the entire history if requested
        if (isClearBackStacks) {
            // Empty all stacks so there is no back history left
            state.backStacks.values.forEach { it.clear() }
            
            // Put the new route into the correct stack so it becomes the only screen
            if (isTopLevelRoute) {
                state.topLevelRoute = route
                state.backStacks[route]?.add(route)
            } else {
                state.backStacks[state.topLevelRoute]?.add(route)
            }
            return
        }

        // 2. Handle popping back to a specific screen before navigating forward
        if (popUpTo != null) {
            trimBackStackTo(popUpTo, isInclusive)
        }

        // 3. Prevent duplicate navigation if we are already trying to go to the screen we are on
        val currentVisibleScreen = state.backStacks[state.topLevelRoute]?.lastOrNull()
        if (currentVisibleScreen == route && (!isTopLevelRoute || state.topLevelRoute == route)) {
            return
        }

        // 4. Finally, push the new route onto the stack so it becomes visible
        if (isTopLevelRoute) {
            // Switching to a main tab/section
            state.topLevelRoute = route
            // If the stack for this tab is empty, add the root screen
            if (state.backStacks[route]?.isEmpty() == true) {
                state.backStacks[route]?.add(route)
            }
        } else {
            // Just push a normal screen onto the current tab's stack
            state.backStacks[state.topLevelRoute]?.add(route)
        }
    }

    /**
     * Helper function to remove screens from the top of the stack until it finds a specific screen instance or class.
     */
    private fun trimBackStackTo(popUpTo: Any, isInclusive: Boolean) {
        state.backStacks.values.forEach { stack ->
            val targetScreenIndex = findTargetIndex(stack, popUpTo)
            
            if (targetScreenIndex != -1) {
                val removeCount = if (isInclusive) {
                    stack.size - targetScreenIndex 
                } else {
                    stack.size - targetScreenIndex - 1 
                }
                repeat(removeCount) { stack.removeLastOrNull() }
            }
        }
    }

    /**
     * Finds the index of a target screen in a given stack.
     * Supports exact instance, KClass, or accidentally passed Companion object.
     */
    private fun findTargetIndex(stack: List<NavKey>, target: Any): Int {
        val targetClass = when {
            target is KClass<*> -> target.java
            target.javaClass.enclosingClass != null && target.javaClass.simpleName == "Companion" -> target.javaClass.enclosingClass
            else -> null
        }

        return if (targetClass != null) {
            stack.indexOfLast { it.javaClass == targetClass }
        } else {
            stack.indexOf(target)
        }
    }

    /**
     * Navigates back.
     *
     * @param popUpTo Can be an exact screen instance (e.g., DetailRoute(1)), a KClass (e.g., DetailRoute::class), or route companion object. If null, pops back one screen.
     * @param isInclusive If true, and popUpTo is set, it will remove the popUpTo screen as well.
     * @param result Optional data to pass back to the destination screen via [navResult].
     */
    fun navigateBack(
        popUpTo: Any? = null,
        isInclusive: Boolean = false,
        result: Any? = null
    ) {
        // Prevent double-clicking back button too fast (debounce)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < backPressThreshold) return
        lastBackPressTime = currentTime

        // Send result if provided
        if (result != null) {
            _navResult.trySend(result)
        }

        // If popUpTo is specified, pop back to that specific destination
        if (popUpTo != null) {
            val orderedStacks = listOfNotNull(
                state.topLevelRoute to state.backStacks[state.topLevelRoute]
            ) + state.backStacks.filterKeys { it != state.topLevelRoute }.toList()

            for ((routeKey, stack) in orderedStacks) {
                if (stack == null) continue
                val targetIndex = findTargetIndex(stack, popUpTo)

                if (targetIndex != -1) {
                    val removeCount = if (isInclusive) {
                        stack.size - targetIndex
                    } else {
                        stack.size - targetIndex - 1
                    }

                    if (removeCount > 0) {
                        if (isInclusive && targetIndex == 0) {
                            activity?.finish()
                        } else {
                            repeat(removeCount) { stack.removeLastOrNull() }
                        }
                    }

                    if (state.topLevelRoute != routeKey) {
                        state.topLevelRoute = routeKey
                    }
                    return
                }
            }
            return
        }

        // Default: Navigate back one screen
        val currentBackStack = state.backStacks[state.topLevelRoute] ?: return
        val currentVisibleScreen = currentBackStack.lastOrNull() ?: return

        when (currentVisibleScreen) {
            is SplashRoute, is HomeLandingRoute -> activity?.finish()
            else -> currentBackStack.removeLastOrNull()
        }
    }
}