package com.dapascript.mever.feature.setting.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverMenuItem
import com.dapascript.mever.core.common.ui.component.rememberInterstitialAd
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType.BITCOIN
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType.PAYPAL
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import com.dapascript.mever.feature.setting.screen.component.HandleAppreciateDialog

@Composable
internal fun SettingAppreciateScreen(navController: NavController) {
    val context = LocalContext.current
    val interstitialController = rememberInterstitialAd(
        onAdFailToLoad = {
            Toast.makeText(
                context,
                context.getString(R.string.unknown_error_desc),
                Toast.LENGTH_SHORT
            ).show()
        }
    )
    var showAppreciateDialog by remember { mutableStateOf<AppreciateType?>(null) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = "",
            onClickBack = { navController.navigateUp() }
        )
    ) {
        showAppreciateDialog?.let { type ->
            HandleAppreciateDialog(
                context = context,
                appreciateType = type
            ) { showAppreciateDialog = it }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = spacedBy(Dp16)
        ) {
            Text(
                text = stringResource(R.string.appreciate),
                style = typography.h2.copy(fontSize = Sp32),
                color = colorScheme.onPrimary,
                modifier = Modifier.padding(top = Dp16)
            )
            getSettingMenus()[1].menus.map {
                MeverMenuItem(
                    menuArgs = MenuItemArgs(
                        leadingTitle = stringResource(it.leadingTitle),
                        leadingIcon = it.icon,
                        leadingIconBackground = it.iconBackgroundColor,
                        leadingIconSize = Dp40,
                        leadingIconPadding = Dp8
                    )
                ) {
                    when (it.leadingTitle) {
                        R.string.bitcoin -> showAppreciateDialog = BITCOIN
                        R.string.paypal -> showAppreciateDialog = PAYPAL
                        R.string.ads -> interstitialController.showAd()
                    }
                }
            }
        }
    }
}