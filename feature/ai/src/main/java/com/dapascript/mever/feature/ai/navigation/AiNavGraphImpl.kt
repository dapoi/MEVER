package com.dapascript.mever.feature.ai.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiBackgroundRemovalRoute
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageGeneratorResultRoute
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageGeneratorRoute
import com.dapascript.mever.feature.ai.screen.AiBackgroundRemovalScreen
import com.dapascript.mever.feature.ai.screen.AiImageGeneratorResultScreen
import com.dapascript.mever.feature.ai.screen.AiImageGeneratorScreen
import javax.inject.Inject

class AiNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator) {
        entry<AiImageGeneratorRoute> { AiImageGeneratorScreen(navigator) }
        entry<AiImageGeneratorResultRoute> { args ->
            AiImageGeneratorResultScreen(navigator = navigator, args = args)
        }
        entry<AiBackgroundRemovalRoute> { AiBackgroundRemovalScreen(navigator) }
    }
}