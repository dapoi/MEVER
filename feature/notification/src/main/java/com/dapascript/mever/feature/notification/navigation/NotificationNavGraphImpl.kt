package com.dapascript.mever.feature.notification.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraphRoute
import com.dapascript.mever.feature.notification.navigation.route.NotificationLanding
import com.dapascript.mever.feature.notification.screen.NotificationScreen
import javax.inject.Inject

class NotificationNavGraphImpl @Inject constructor() : NotificationNavGraph() {
    override fun buildGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) {
        navGraphBuilder.navigation<NotificationNavGraphRoute>(startDestination = NotificationLanding) {
            composable<NotificationLanding> { NotificationScreen(navigator) }
        }
    }

    override fun getNotificationRoute() = NotificationLanding
}