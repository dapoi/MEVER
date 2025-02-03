package com.dapascript.mever.feature.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.extension.composableScreen
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraphRoute
import com.dapascript.mever.feature.notification.navigation.route.NotificationLandingRoute
import com.dapascript.mever.feature.notification.screen.NotificationLandingScreen
import javax.inject.Inject

class NotificationNavGraphImpl @Inject constructor() : NotificationNavGraph() {
    override fun createGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NotificationNavGraphRoute>(startDestination = NotificationLandingRoute) {
            composableScreen<NotificationLandingRoute> { NotificationLandingScreen(navigator) }
        }
    }

    override fun getNotificationLandingRoute() = NotificationLandingRoute
}