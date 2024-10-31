package com.dapascript.mever.feature.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.ui.BaseScreen
import com.dapascript.mever.core.common.ui.attr.ActionMenuAttr.ActionMenu
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.util.Constant.EMPTY_STRING
import com.dapascript.mever.core.common.util.Constant.ScreenName.EXPLORE
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.feature.home.R

@Composable
fun HomeScreen(
    navigator: BaseNavigator
) {
    val webDomain = remember { mutableStateOf(TextFieldValue()) }
    val listOfActionMenu = mapOf(
        NOTIFICATION to R.drawable.ic_notification,
        EXPLORE to R.drawable.ic_explore,
        SETTING to R.drawable.ic_setting
    )
    val onClickActionMenu = remember {
        { name: String ->
            // Handle action menu click
            when (name) {
                NOTIFICATION -> {
                    navigator.run {
                        navigate(getNavGraph<NotificationNavGraph>().getNotificationRoute())
                    }
                }

                EXPLORE -> {
                    // Handle explore click
                }

                SETTING -> {
                    navigator.run {
                        navigate(getNavGraph<SettingNavGraph>().getSettingRoute())
                    }
                }

                else -> Unit
            }
        }
    }

    BaseScreen(
        screenName = EMPTY_STRING,
        isHome = true,
        listMenuAction = listOfActionMenu.map { (name, resource) ->
            ActionMenu(resource = resource, name = name, isShowBadge = name == NOTIFICATION)
        },
        onClickActionMenu = { name -> onClickActionMenu(name) }
    ) {
        Column(
            modifier = Modifier.padding(top = Dp32),
            horizontalAlignment = CenterHorizontally
        ) {
            MeverTextField(webDomainValue = webDomain.value) {
                webDomain.value = it
            }
        }
    }
}