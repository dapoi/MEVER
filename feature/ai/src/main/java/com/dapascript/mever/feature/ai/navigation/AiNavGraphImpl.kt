package com.dapascript.mever.feature.ai.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.route.AiScreenRoute.AiImageResultRoute
import com.dapascript.mever.feature.ai.screen.AiImageResultScreen
import javax.inject.Inject

class AiNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = navGraphBuilder.composableScreen<AiImageResultRoute> {
        AiImageResultScreen(navController)
    }
}