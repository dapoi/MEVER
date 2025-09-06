package com.dapascript.mever.feature.setting.screen

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.text.format.Formatter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs.TrailingType.Default
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs.TrailingType.Switch
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverMenuItem
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp28
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverOrange
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverRed
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp20
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.LanguageManager.getLanguageCode
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.navigateToGmail
import com.dapascript.mever.core.common.util.navigateToNotificationSettings
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.common.util.storage.StorageUtil.StorageInfo
import com.dapascript.mever.core.navigation.helper.navigateTo
import com.dapascript.mever.core.navigation.route.SettingScreenRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAboutAppRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute.LanguageData
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType.BITCOIN
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType.PAYPAL
import com.dapascript.mever.feature.setting.screen.component.HandleAppreciateDialog
import com.dapascript.mever.feature.setting.screen.component.HandleBottomSheetQris
import com.dapascript.mever.feature.setting.viewmodel.SettingLandingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
internal fun SettingLandingScreen(
    navController: NavController,
    deviceType: DeviceType,
    viewModel: SettingLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val themeType = themeType.collectAsStateValue()
    val isPipEnabled = isPipEnabled.collectAsStateValue()
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val showAppreciateDialog = remember { mutableStateOf<AppreciateType?>(null) }
    val statusColor = remember(storageInfo.usedPercent) {
        getStatusStorageColor(storageInfo.usedPercent)
    }
    val isExpanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 &&
                    listState.firstVisibleItemScrollOffset < titleHeight / 2
        }
    }
    var showBottomSheetQris by remember { mutableStateOf(false) }
    var setRequestPermission by remember { mutableStateOf<List<String>>(emptyList()) }
    val usedStorage by animateFloatAsState(
        targetValue = animatedPercent,
        animationSpec = tween(durationMillis = 1000)
    )

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = if (isExpanded.not()) stringResource(R.string.settings) else "",
            onClickBack = { navController.popBackStack() }
        ),
        allowScreenOverlap = true
    ) {
        LaunchedEffect(listState, titleHeight) {
            delay(1000L)
            snapshotFlow { listState.isScrollInProgress }
                .distinctUntilChanged()
                .filter { it.not() }
                .collect {
                    if (titleHeight == 0 || listState.firstVisibleItemIndex > 0) return@collect

                    val threshold = titleHeight / 2
                    val currentOffset = listState.firstVisibleItemScrollOffset

                    if (currentOffset in 1 until titleHeight) {
                        val targetIndex = if (currentOffset < threshold) 0 else 1
                        listState.animateScrollToItem(targetIndex)
                    }
                }
        }

        LaunchedEffect(storageInfo.usedPercent) {
            delay(350)
            animatedPercent = storageInfo.usedPercent / 100f
        }

        LaunchedEffect(context) { getLanguageCode = getLanguageCode(context) }

        if (setRequestPermission.isNotEmpty()) {
            MeverPermissionHandler(
                permissions = setRequestPermission,
                onGranted = { setRequestPermission = emptyList() },
                onDenied = { isPermanentlyDeclined, onRetry ->
                    MeverDialog(
                        showDialog = true,
                        meverDialogArgs = MeverDialogArgs(
                            title = stringResource(R.string.permission_request_title),
                            primaryButtonText = stringResource(
                                if (isPermanentlyDeclined) R.string.go_to_settings
                                else R.string.allow
                            ),
                            onClickPrimaryButton = {
                                if (isPermanentlyDeclined) {
                                    setRequestPermission = emptyList()
                                    navigateToNotificationSettings(context)
                                } else onRetry()
                            },
                            onClickSecondaryButton = { setRequestPermission = emptyList() }
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.permission_request_notification),
                            textAlign = Center,
                            style = typography.body1,
                            color = colorScheme.onPrimary,
                            modifier = Modifier.padding(vertical = Dp8)
                        )
                    }
                }
            )
        }

        showAppreciateDialog.value?.let { type ->
            HandleAppreciateDialog(
                context = context,
                appreciateType = type
            ) { showAppreciateDialog.value = it }
        }

        HandleBottomSheetQris(showBottomSheetQris) { showBottomSheetQris = it }

        SettingLandingContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64)
                .systemBarsPadding(),
            context = context,
            storageInfo = storageInfo,
            usedStorage = usedStorage,
            statusColor = statusColor,
            deviceType = deviceType,
            viewModel = this,
            listState = listState,
            isExpanded = isExpanded,
            isPipEnabled = isPipEnabled,
            getLanguageCode = getLanguageCode,
            themeType = themeType,
            onClickChangeLanguage = {
                navController.navigateTo(SettingLanguageRoute(LanguageData(it)))
            },
            onClickNotificationPermission = {
                val perm = getNotificationPermission().firstOrNull()
                if (perm != null && context.checkSelfPermission(perm) != PERMISSION_GRANTED) {
                    setRequestPermission = listOf(perm)
                } else navigateToNotificationSettings(context)
            },
            onClickChangeTheme = { navController.navigate(SettingScreenRoute.SettingThemeRoute(it)) },
            onClickPip = { savePipState(isPipEnabled.not()) },
            onClickDonate = { showAppreciateDialog.value = it },
            onClickQris = { showBottomSheetQris = true },
            onClickContact = { navigateToGmail(context) },
            onClickAbout = { navController.navigateTo(SettingAboutAppRoute) }
        )
    }
}

@Composable
private fun SettingLandingContent(
    context: Context,
    storageInfo: StorageInfo,
    usedStorage: Float,
    statusColor: Color,
    deviceType: DeviceType,
    viewModel: SettingLandingViewModel,
    listState: LazyListState,
    isExpanded: Boolean,
    isPipEnabled: Boolean,
    getLanguageCode: String,
    themeType: ThemeType,
    modifier: Modifier = Modifier,
    onClickChangeLanguage: (String) -> Unit,
    onClickNotificationPermission: () -> Unit,
    onClickChangeTheme: (ThemeType) -> Unit,
    onClickPip: () -> Unit,
    onClickDonate: (AppreciateType) -> Unit,
    onClickQris: () -> Unit,
    onClickContact: () -> Unit,
    onClickAbout: () -> Unit
) = with(viewModel) {
    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        Column(modifier = modifier) {
            if (isExpanded.not() && titleHeight > 0) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(Dp3),
                    thickness = Dp1,
                    color = colorScheme.onPrimary.copy(alpha = 0.12f)
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = listState
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { titleHeight = it.size.height }
                    ) {
                        Spacer(modifier = Modifier.height(Dp16))
                        Text(
                            text = stringResource(R.string.settings),
                            style = typography.h2.copy(fontSize = Sp32),
                            color = colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = Dp24)
                        )
                    }
                }
                item {
                    AvailableStorageSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp24, vertical = Dp32),
                        context = context,
                        storageInfo = storageInfo,
                        usedStorage = usedStorage,
                        statusColor = statusColor,
                        deviceType = deviceType
                    )
                }
                settingMenus.forEach { (title, menus) ->
                    item {
                        Text(
                            text = stringResource(title),
                            style = typography.h3,
                            color = colorScheme.onPrimary,
                            modifier = Modifier.padding(start = Dp24, end = Dp24, bottom = Dp12)
                        )
                    }
                    items(
                        items = menus,
                        key = { menu -> menu.leadingTitle }
                    ) { menu ->
                        MeverMenuItem(
                            modifier = Modifier.padding(horizontal = Dp24),
                            menuArgs = MenuItemArgs(
                                leadingIcon = menu.icon,
                                leadingIconBackground = menu.iconBackgroundColor,
                                leadingTitle = stringResource(menu.leadingTitle),
                                leadingIconSize = Dp40,
                                leadingIconPadding = Dp8,
                                trailingType = if (menu.leadingTitle != R.string.pip) {
                                    Default(
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
                                    )
                                } else Switch(isPipEnabled)
                            )
                        ) {
                            handleClickMenu(
                                context = context,
                                title = context.getString(menu.leadingTitle),
                                languageCode = getLanguageCode,
                                themeType = themeType,
                                onClickChangeLanguage = { onClickChangeLanguage(it) },
                                onClickNotificationPermission = { onClickNotificationPermission() },
                                onClickChangeTheme = { onClickChangeTheme(it) },
                                onClickPip = { onClickPip() },
                                onClickDonate = { onClickDonate(it) },
                                onClickQris = { onClickQris() },
                                onClickContact = { onClickContact() },
                                onClickAbout = { onClickAbout() }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(Dp28)) }
                }
            }
        }
    }
}

@Composable
private fun AvailableStorageSection(
    context: Context,
    storageInfo: StorageInfo,
    usedStorage: Float,
    statusColor: Color,
    deviceType: DeviceType,
    modifier: Modifier = Modifier
) = with(storageInfo) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = if (deviceType == PHONE) SpaceEvenly else spacedBy(Dp24)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(if (deviceType == PHONE) Dp120 else Dp150),
                    progress = { usedStorage },
                    color = statusColor,
                    strokeWidth = Dp8,
                    trackColor = colorScheme.onBackground.copy(alpha = 0.1f),
                    strokeCap = Round,
                    gapSize = Dp0
                )
                Text(
                    text = "$usedPercent%",
                    style = typography.body1.copy(fontSize = Sp20),
                    color = colorScheme.onBackground
                )
            }
            Column(verticalArrangement = spacedBy(Dp4)) {
                Text(
                    text = stringResource(R.string.storage),
                    style = typography.bodyBold1,
                    color = statusColor
                )
                Text(
                    text = getUsedOfTotalText(context, this@with),
                    style = typography.body2,
                    color = colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun getUsedOfTotalText(context: Context, storageInfo: StorageInfo): String {
    val usedText = Formatter.formatFileSize(context, storageInfo.usedBytes).replace(" ", "")
    val totalText = Formatter.formatFileSize(context, storageInfo.totalBytes).replace(" ", "")
    return stringResource(R.string.used_storage, usedText, totalText)
}

private fun getStatusStorageColor(usedPercent: Int) = when {
    usedPercent < 70 -> MeverPurple
    usedPercent in 70..90 -> MeverOrange
    else -> MeverRed
}

private fun handleClickMenu(
    context: Context,
    title: String,
    languageCode: String,
    themeType: ThemeType,
    onClickChangeLanguage: (String) -> Unit,
    onClickNotificationPermission: () -> Unit,
    onClickChangeTheme: (ThemeType) -> Unit,
    onClickPip: () -> Unit,
    onClickDonate: (AppreciateType) -> Unit,
    onClickQris: () -> Unit,
    onClickContact: () -> Unit,
    onClickAbout: () -> Unit
) = with(context) {
    when (title) {
        getString(R.string.language) -> onClickChangeLanguage(languageCode)
        getString(R.string.notification) -> onClickNotificationPermission()
        getString(R.string.theme) -> onClickChangeTheme(themeType)
        getString(R.string.pip) -> onClickPip()
        getString(R.string.bitcoin) -> onClickDonate(BITCOIN)
        getString(R.string.paypal) -> onClickDonate(PAYPAL)
        getString(R.string.qris) -> onClickQris()
        getString(R.string.contact) -> onClickContact()
        getString(R.string.about) -> onClickAbout()
    }
}