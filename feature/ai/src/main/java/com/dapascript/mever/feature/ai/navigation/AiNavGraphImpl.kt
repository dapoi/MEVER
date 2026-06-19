package com.dapascript.mever.feature.ai.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageResultRoute
import com.dapascript.mever.feature.ai.screen.AiImageResultScreen
import javax.inject.Inject

class AiNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<AiImageResultRoute> { args ->
            AiImageResultScreen(navigator = navigator, args = args)
        }
    }
}