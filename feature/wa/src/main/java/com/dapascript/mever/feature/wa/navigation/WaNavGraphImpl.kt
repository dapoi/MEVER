package com.dapascript.mever.feature.wa.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.WaScreenRoute.WaStatusLandingRoute
import com.dapascript.mever.feature.wa.screen.WaStatusLandingScreen
import javax.inject.Inject

class WaNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<WaStatusLandingRoute> { WaStatusLandingScreen(navigator) }
    }
}