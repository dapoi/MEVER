package com.dapascript.mever.feature.setting.screen

import android.content.Context
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverMenuItem
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp28
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.navigateToGmail
import com.dapascript.mever.core.common.util.navigateToNotificationSettings
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute.LanguageData
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingThemeRoute
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.BTC_ADDRESS
import com.dapascript.mever.feature.setting.viewmodel.SettingLandingViewModel

@Composable
internal fun SettingLandingScreen(
    navController: NavController,
    viewModel: SettingLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val getLanguageCode = getLanguageCode.collectAsStateValue()
    val themeType = themeType.collectAsStateValue()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scrollState = rememberScrollState()
    val showBitcoinAddressDialog = remember { mutableStateOf(false) }
    val isExpanded by remember { derivedStateOf { scrollState.value <= titleHeight } }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = if (isExpanded.not()) stringResource(R.string.settings) else "",
            onClickBack = { navController.popBackStack() }
        ),
        allowScreenOverlap = true
    ) {
        MeverDialog(
            showDialog = showBitcoinAddressDialog.value,
            meverDialogArgs = MeverDialogArgs(
                title = stringResource(R.string.bitcoin_address),
                primaryButtonText = stringResource(R.string.copy),
                onClickPrimaryButton = {
                    clipboardManager.setText(AnnotatedString(BTC_ADDRESS))
                    showBitcoinAddressDialog.value = false
                },
                onClickSecondaryButton = { showBitcoinAddressDialog.value = false }
            )
        ) {
            Text(
                text = BTC_ADDRESS,
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }

        SettingLandingContent(
            context = context,
            viewModel = this,
            isExpanded = isExpanded,
            scrollState = scrollState,
            getLanguageCode = getLanguageCode,
            themeType = themeType,
            navController = navController,
            showBitcoinAddressDialog = showBitcoinAddressDialog
        )
    }
}

@Composable
private fun SettingLandingContent(
    context: Context,
    viewModel: SettingLandingViewModel,
    isExpanded: Boolean,
    scrollState: ScrollState,
    getLanguageCode: String,
    themeType: ThemeType,
    navController: NavController,
    showBitcoinAddressDialog: MutableState<Boolean>
) = with(viewModel) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dp64)
            .systemBarsPadding()
    ) {
        if (isExpanded.not()) HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dp1)
                .shadow(Dp3),
            thickness = Dp1,
            color = colorScheme.onPrimary.copy(alpha = 0.12f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Dp24)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(Dp16))
            Text(
                text = stringResource(R.string.settings),
                style = typography.h2.copy(fontSize = Sp32),
                color = colorScheme.onPrimary,
                modifier = Modifier.onGloballyPositioned { titleHeight = it.size.height }
            )
            Spacer(modifier = Modifier.height(Dp32))
            Column(
                modifier = Modifier
                    .height(this@BoxWithConstraints.maxHeight)
                    .nestedScroll(
                        object : NestedScrollConnection {
                            override fun onPreScroll(
                                available: Offset,
                                source: NestedScrollSource
                            ) = if (available.y > 0 || isExpanded) Offset.Zero
                            else Offset(x = 0f, y = -scrollState.dispatchRawDelta(-available.y))
                        }
                    )
            ) {
                CompositionLocalProvider(LocalOverscrollFactory provides null) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        settingMenus.forEach { (title, menus) ->
                            item {
                                Text(
                                    text = stringResource(title),
                                    style = typography.h3,
                                    color = colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.height(Dp12))
                            }
                            items(
                                items = menus,
                                key = { menu -> menu.leadingTitle }
                            ) { menu ->
                                MeverMenuItem(
                                    menuArgs = MenuItemArgs(
                                        leadingIcon = menu.icon,
                                        leadingIconBackground = menu.iconBackgroundColor,
                                        leadingTitle = stringResource(menu.leadingTitle),
                                        leadingIconSize = Dp40,
                                        leadingIconPadding = Dp8,
                                        trailingTitle = menu.trailingTitle?.let {
                                            when (stringResource(menu.leadingTitle)) {
                                                stringResource(R.string.language) -> {
                                                    if (getLanguageCode == "en") "English"
                                                    else "Bahasa Indonesia"
                                                }

                                                stringResource(R.string.theme) -> stringResource(
                                                    themeType.themeResId
                                                )

                                                else -> it
                                            }
                                        }
                                    ),
                                    modifier = Modifier.animateItem()
                                ) {
                                    navController.handleClickMenu(
                                        context = context,
                                        title = context.getString(menu.leadingTitle),
                                        languageCode = getLanguageCode,
                                        themeType = themeType,
                                        showBitcoinAddressDialog = showBitcoinAddressDialog
                                    )
                                }
                            }
                            item { Spacer(modifier = Modifier.height(Dp28)) }
                        }
                    }
                }
            }
        }
    }
}

private fun NavController.handleClickMenu(
    context: Context,
    title: String,
    languageCode: String,
    themeType: ThemeType,
    showBitcoinAddressDialog: MutableState<Boolean>
) = with(context) {
    when (title) {
        getString(R.string.language) -> navigateTo(SettingLanguageRoute(LanguageData(languageCode)))
        getString(R.string.notification) -> navigateToNotificationSettings(this)
        getString(R.string.theme) -> navigateTo(SettingThemeRoute(themeType))
        getString(R.string.bitcoin) -> showBitcoinAddressDialog.value = true
        getString(R.string.contact) -> navigateToGmail(this)
    }
}