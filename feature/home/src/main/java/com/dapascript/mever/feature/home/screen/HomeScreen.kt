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
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.ui.BaseScreen
import com.dapascript.mever.core.common.ui.attr.ActionMenuAttr.ActionMenu
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.feature.home.R

@Composable
fun HomeScreen(
    navigator: BaseNavigator
) {
    val webDomain = remember { mutableStateOf(TextFieldValue()) }
    val listOfActionMenu = mapOf(
        "Notification" to R.drawable.ic_notification,
        "Explore" to R.drawable.ic_explore,
        "Setting" to R.drawable.ic_setting
    )
    val onClickActionMenu = remember {
        { name: String ->
            // Handle action menu click
            when (name) {
                "Notification" -> {
                    // Handle notification click
                }

                "Explore" -> {
                    // Handle explore click
                }

                "Setting" -> {
                    // Handle setting click
                    navigator.run {
                        navigate(getNavGraph<SettingNavGraph>().getSettingRoute())
                    }
                }

                else -> Unit
            }
        }
    }

    BaseScreen(
        screenName = "",
        isHome = true,
        listMenuAction = listOfActionMenu.map { (name, resource) ->
            ActionMenu(resource = resource, name = name, isShowBadge = name == "Notification")
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