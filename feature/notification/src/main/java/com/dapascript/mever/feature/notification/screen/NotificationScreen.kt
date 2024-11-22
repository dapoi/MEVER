package com.dapascript.mever.feature.notification.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.feature.notification.viewmodel.NotificationViewModel

@Composable
fun NotificationScreen(
    navigator: BaseNavigator,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    BaseScreen(
        screenName = NOTIFICATION,
        actionMenus = emptyList(),
        onClickBack = { navigator.popBackStack() }
    ) {

    }
}