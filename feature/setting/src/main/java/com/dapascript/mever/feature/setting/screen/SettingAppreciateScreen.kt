package com.dapascript.mever.feature.setting.screen

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs.TrailingType.Default
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverMenuItem
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.copyToClipboard
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import com.dapascript.mever.feature.setting.screen.component.HandleBottomSheetQris

@Composable
internal fun SettingAppreciateScreen(navigator: Navigator) {
    val context = LocalContext.current
    val resources = LocalResources.current
    var showPaypalDialog by remember { mutableStateOf(false) }
    var showBottomSheetQris by remember { mutableStateOf(false) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = "",
            onClickBack = { navigator.goBack() }
        )
    ) {
        MeverDialog(
            showDialog = showPaypalDialog,
            image = null,
            title = stringResource(R.string.paypal_email),
            description = stringResource(R.string.email),
            primaryActionLabel = stringResource(R.string.copy),
            onClickPrimaryAction = {
                copyToClipboard(context, resources.getString(R.string.email))
                showPaypalDialog = false
            },
            onClickSecondaryAction = { showPaypalDialog = false }
        )

        HandleBottomSheetQris(showBottomSheetQris) { showBottomSheetQris = it }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dp24),
            verticalArrangement = spacedBy(Dp16)
        ) {
            Text(
                modifier = Modifier.padding(top = Dp80),
                text = stringResource(R.string.appreciate),
                style = typography.h2.copy(fontSize = Sp32),
                color = colors.blackWhite
            )
            getSettingMenus(context)[1].menus.forEach {
                MeverMenuItem(
                    menuArgs = MenuItemArgs(
                        leadingTitle = it.leadingTitle,
                        leadingIcon = it.icon,
                        leadingIconBackground = it.iconBackgroundColor,
                        leadingIconSize = Dp40,
                        leadingIconPadding = Dp8,
                        trailingType = Default()
                    )
                ) {
                    when (it.leadingTitle) {
                        resources.getString(R.string.paypal) -> showPaypalDialog = true
                        resources.getString(R.string.qris) -> showBottomSheetQris = true
                    }
                }
            }
        }
    }
}