package com.dapascript.mever.feature.setting.screen

import androidx.compose.runtime.Composable
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.base.BaseScreen

@Composable
fun SettingScreen(
    navigator: BaseNavigator
) {

    BaseScreen(
        screenName = "Setting",
        actionMenus = emptyList(),
        onClickBack = { navigator.popBackStack() }
    ) { }
}