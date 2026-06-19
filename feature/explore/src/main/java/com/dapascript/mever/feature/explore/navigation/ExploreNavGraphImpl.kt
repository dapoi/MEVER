package com.dapascript.mever.feature.explore.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.ExploreScreenRoute.ExploreLandingRoute
import com.dapascript.mever.feature.explore.screen.ExploreLandingScreen
import javax.inject.Inject

class ExploreNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<ExploreLandingRoute> { ExploreLandingScreen(navigator) }
    }
}