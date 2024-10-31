package com.dapascript.mever.feature.notification.screen

import androidx.compose.runtime.Composable
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.BaseScreen

@Composable
fun NotificationScreen(
    navigator: BaseNavigator
) {

    BaseScreen(
        screenName = "Notification",
        listMenuAction = emptyList(),
        onClickBack = { navigator.popBackStack() }
    ) { }
}