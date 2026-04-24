package com.dapascript.mever.feature.wa.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.core.navigation.helper.composableScreen
import com.dapascript.mever.core.navigation.route.WaScreenRoute.WaStatusRoute
import com.dapascript.mever.feature.wa.screen.WaStatusScreen
import javax.inject.Inject

class WaNavGraphImpl @Inject constructor() : BaseNavGraph {
    override fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    ) = navGraphBuilder.composableScreen<WaStatusRoute> {
        WaStatusScreen(navController)
    }
}