package com.dapascript.mever.feature.setting.screen

import androidx.compose.runtime.Composable
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING

@Composable
internal fun SettingLandingScreen(
    navigator: BaseNavigator
) {
    BaseScreen(
        TopBarArgs(
            screenName = SETTING,
            onClickBack = { navigator.popBackStack() }
        )
    ) { }
}