package com.dapascript.mever.core.navigation

import androidx.compose.animation.core.Spring.StiffnessMedium
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.recreateActivity
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.NavigationState
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute
import com.dapascript.mever.core.navigation.route.StartupScreenRoute.SplashRoute
import kotlinx.coroutines.flow.Flow

@Composable
fun MainNavigation(
    navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>,
    navigationToHomeEvent: Flow<Unit>? = null
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val navigationState = rememberNavigationState(
        startRoute = SplashRoute,
        topLevelRoutes = setOf(SplashRoute, HomeLandingRoute)
    )
    val navigator = remember(activity) { Navigator(navigationState, activity) }
    val entryProvider = remember(navGraphs, navigator) {
        entryProvider {
            navGraphs.forEach { navGraph ->
                with(navGraph) { createGraph(navigator) }
            }
        }
    }

    LaunchedEffect(navigationToHomeEvent) {
        navigationToHomeEvent?.collect {
            if (navigationState.currentRoute != SplashRoute) recreateActivity(context, activity)
        }
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        transitionSpec = {
            horizontalSlide(initialOffsetX = { it }, targetOffsetX = { -it })
        },
        popTransitionSpec = {
            horizontalSlide(initialOffsetX = { -it }, targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            horizontalSlide(initialOffsetX = { -it }, targetOffsetX = { it })
        }
    )
}

private val NavSpringSpec = spring(
    stiffness = StiffnessMedium,
    visibilityThreshold = IntOffset.VisibilityThreshold
)

private fun horizontalSlide(
    initialOffsetX: (Int) -> Int,
    targetOffsetX: (Int) -> Int
) = slideInHorizontally(
    animationSpec = NavSpringSpec,
    initialOffsetX = initialOffsetX
) togetherWith slideOutHorizontally(
    animationSpec = NavSpringSpec,
    targetOffsetX = targetOffsetX
)

@Composable
private fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>
): NavigationState {
    val topLevelRoute = rememberSerializable(
        startRoute, topLevelRoutes,
        serializer = MutableStateSerializer(NavKeySerializer())
    ) { mutableStateOf(startRoute) }

    val backStacks = topLevelRoutes.associateWith { key -> rememberNavBackStack(key) }

    return remember(startRoute, topLevelRoutes) {
        NavigationState(
            startRoute = startRoute,
            topLevelRoute = topLevelRoute,
            backStacks = backStacks
        )
    }
}

private val NavigationState.currentRoute: NavKey?
    get() = backStacks[topLevelRoute]?.lastOrNull()

@Composable
private fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): List<NavEntry<NavKey>> {
    val decorators = listOf<NavEntryDecorator<NavKey>>(
        rememberSaveableStateHolderNavEntryDecorator(),
        rememberViewModelStoreNavEntryDecorator()
    )

    val decoratedEntries = backStacks.mapValues { (_, stack) ->
        rememberDecoratedNavEntries(
            backStack = stack,
            entryProvider = entryProvider,
            entryDecorators = decorators
        )
    }

    return remember(decoratedEntries, stacksInUse) {
        stacksInUse.flatMap { decoratedEntries[it] ?: emptyList() }
    }
}