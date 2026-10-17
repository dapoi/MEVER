package com.dapascript.mever.feature.explore.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.ExploreScreenRoute.ArtImageRoute
import com.dapascript.mever.core.navigation.route.ExploreScreenRoute.ImageFinderRoute
import com.dapascript.mever.feature.explore.screen.ArtImageScreen
import com.dapascript.mever.feature.explore.screen.ImageFinderScreen
import javax.inject.Inject

internal class ExploreNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<ImageFinderRoute> { ImageFinderScreen(navigator) }
        entry<ArtImageRoute> { ArtImageScreen(navigator) }
    }
}