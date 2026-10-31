package com.dapascript.mever.feature.setting.screen

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs.TrailingType.Default
import com.dapascript.mever.core.common.ui.attr.MeverMenuItemAttr.MenuItemArgs.TrailingType.Switch
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverMenuItem
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.component.meverShimmer
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp100
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp130
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp28
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp60
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp62
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.FadeSide.Bottom
import com.dapascript.mever.core.common.util.LanguageManager.getLanguageCode
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.cleanCache
import com.dapascript.mever.core.common.util.copyToClipboard
import com.dapascript.mever.core.common.util.fadingEdge
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.navigateToGmail
import com.dapascript.mever.core.common.util.navigateToNotificationSettings
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAboutAppRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingFaqRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLandingRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingPaywallRoute
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.SettingMenus
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import com.dapascript.mever.feature.setting.screen.component.HandleBottomSheetQris
import com.dapascript.mever.feature.setting.screen.component.MeverProBannerCard
import com.dapascript.mever.feature.setting.viewmodel.SettingLandingViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
internal fun SettingLandingScreen(
    navigator: Navigator,
    args: SettingLandingRoute,
    viewModel: SettingLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val isInitialLoading = isInitialLoading.collectAsStateValue()
    val isProActive = isProActive.collectAsStateValue()
    val themeType = themeType.collectAsStateValue()
    val isPipEnabled = isPipEnabled.collectAsStateValue()
    val isQuickNotifEnabled = isQuickNotifEnabled.collectAsStateValue()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var showPaypalDialog by remember { mutableStateOf(false) }
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    val isExpanded = remember(listState, titleHeight) {
        derivedStateOf {
            if (titleHeight == 0) return@derivedStateOf true
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset < (titleHeight / 2)
        }
    }
    var showBottomSheetQris by rememberSaveable { mutableStateOf<Boolean?>(null) }
    var checkPermissions by remember { mutableStateOf<List<String>>(emptyList()) }

    BaseScreen(
        topBarArgs = TopBarArgs(
            title = if (isExpanded.value.not() && isInitialLoading.not()) stringResource(R.string.settings) else ""
        ),
        onBackHandler = { navigator.navigateBack() }
    ) {
        val menus = remember(context, appVersion) {
            getSettingMenus(context, appVersion)
        }

        LaunchedEffect(listState, titleHeight) {
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

        LaunchedEffect(context) { languageCode = getLanguageCode(context) }

        LaunchedEffect(args.showQrisDialog, isInitialLoading) {
            if (showBottomSheetQris == null && args.showQrisDialog && isInitialLoading.not()) {
                val totalItems = listState.layoutInfo.totalItemsCount
                if (totalItems > 0) listState.scrollToItem(totalItems - 1)
                showBottomSheetQris = true
            }
        }

        if (checkPermissions.isNotEmpty()) {
            MeverPermissionHandler(
                permissions = checkPermissions,
                onGranted = { checkPermissions = emptyList() },
                onDenied = { isPermanentlyDeclined, onRetry ->
                    MeverDialog(
                        showDialog = true,
                        title = stringResource(R.string.permission_request_title),
                        description = stringResource(R.string.permission_request_notification),
                        primaryActionLabel = stringResource(
                            if (isPermanentlyDeclined) R.string.go_to_settings
                            else R.string.allow
                        ),
                        onClickPrimaryAction = {
                            if (isPermanentlyDeclined) {
                                checkPermissions = emptyList()
                                navigateToNotificationSettings(context)
                            } else onRetry()
                        },
                        onClickSecondaryAction = { checkPermissions = emptyList() }
                    )
                }
            )
        }

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

        HandleBottomSheetQris(showBottomSheetQris == true) { showBottomSheetQris = it }

        LaunchedEffect(Unit) {
            checkSubscriptionEntitlement()
        }

        AnimatedVisibility(
            visible = isInitialLoading.not(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            SettingLandingContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dp64),
                context = context,
                listState = listState,
                isPipEnabled = isPipEnabled,
                isQuickNotifEnabled = isQuickNotifEnabled,
                getLanguageCode = languageCode,
                themeType = themeType,
                isProActive = isProActive,
                menus = menus,
                isExpanded = { isExpanded.value },
                onSetTitleHeight = { titleHeight = it },
                onClickProAccess = { navigator.navigate(SettingPaywallRoute) },
                onClickChangeLanguage = { languageCode ->
                    navigator.navigate(SettingLanguageRoute(languageCode))
                },
                onClickNotificationPermission = {
                    val notifPermission = getNotificationPermission()
                    if (notifPermission.any { context.checkSelfPermission(it) != PERMISSION_GRANTED }) {
                        checkPermissions = notifPermission
                    } else navigateToNotificationSettings(context)
                },
                onClickChangeTheme = { navigator.navigate(SettingScreenRoute.SettingThemeRoute(it)) },
                onClickCleanCache = {
                    scope.launch { cleanCache(context, activity) }
                },
                onClickPip = { savePipState(isPipEnabled.not()) },
                onClickQuickNotif = {
                    if (isProActive) {
                        saveQuickNotifState(isQuickNotifEnabled.not())
                    } else {
                        navigator.navigate(SettingPaywallRoute)
                    }
                },
                onClickPaypal = { showPaypalDialog = true },
                onClickQris = { showBottomSheetQris = true },
                onClickFaq = { navigator.navigate(SettingFaqRoute) },
                onClickContact = { navigateToGmail(context) },
                onClickAbout = { navigator.navigate(SettingAboutAppRoute) }
            )
        }

        if (isInitialLoading) {
            SettingShimmer()
        }
    }
}

@Composable
private fun SettingLandingContent(
    context: Context,
    listState: LazyListState,
    isPipEnabled: Boolean,
    isQuickNotifEnabled: Boolean,
    getLanguageCode: String,
    themeType: ThemeType,
    isProActive: Boolean,
    menus: List<SettingMenus>,
    modifier: Modifier = Modifier,
    isExpanded: () -> Boolean,
    onSetTitleHeight: (Int) -> Unit = {},
    onClickProAccess: () -> Unit,
    onClickChangeLanguage: (String) -> Unit,
    onClickNotificationPermission: () -> Unit,
    onClickChangeTheme: (ThemeType) -> Unit,
    onClickCleanCache: () -> Unit,
    onClickPip: () -> Unit,
    onClickQuickNotif: () -> Unit,
    onClickPaypal: () -> Unit,
    onClickQris: () -> Unit,
    onClickFaq: () -> Unit,
    onClickContact: () -> Unit,
    onClickAbout: () -> Unit
) = CompositionLocalProvider(LocalOverscrollFactory provides null) {
    val showBottomFade by remember {
        derivedStateOf { listState.canScrollForward }
    }

    Column(
        modifier = modifier.fadingEdge(
            side = Bottom,
            isVisible = showBottomFade
        )
    ) {
        if (isExpanded().not()) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = Dp1,
                color = colors.blackWhite.copy(alpha = 0.12f)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(bottom = Dp32)
        ) {
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(Dp16))
                    AnimatedVisibility(
                        visible = isExpanded(),
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            text = stringResource(R.string.settings),
                            style = typography.h2.copy(fontSize = Sp32),
                            color = colors.blackWhite,
                            modifier = Modifier
                                .padding(horizontal = Dp24)
                                .onGloballyPositioned { onSetTitleHeight(it.size.height) }
                        )
                    }
                }
            }
            item {
                MeverProBannerCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = Dp24, end = Dp24, top = Dp24, bottom = Dp32),
                    isProActive = isProActive,
                    onClickUpgrade = onClickProAccess
                )
            }
            menus.forEach { (title, menus) ->
                item {
                    Text(
                        text = stringResource(title),
                        style = typography.h3,
                        color = colors.blackWhite,
                        modifier = Modifier.padding(start = Dp24, end = Dp24, bottom = Dp12)
                    )
                }
                items(
                    items = menus,
                    key = { menu -> menu.leadingTitle },
                    contentType = { "menu_item" }
                ) { menu ->
                    val trailingTitle = remember(menu, getLanguageCode, themeType) {
                        menu.trailingTitle?.let {
                            when (menu.leadingTitle) {
                                context.getString(R.string.language) -> {
                                    if (getLanguageCode == "en") "English"
                                    else "Bahasa Indonesia"
                                }

                                context.getString(R.string.theme) -> context.getString(
                                    themeType.themeResId
                                )

                                else -> it
                            }
                        }
                    }

                    val menuArgs = remember(menu, trailingTitle, isPipEnabled, isQuickNotifEnabled, isProActive) {
                        MenuItemArgs(
                            leadingIcon = menu.icon,
                            leadingIconBackground = menu.iconBackgroundColor,
                            leadingTitle = menu.leadingTitle,
                            leadingDesc = menu.leadingDesc,
                            leadingIconSize = Dp40,
                            leadingIconPadding = Dp8,
                            enabled = when (menu.leadingTitle) {
                                context.getString(R.string.quick_download_notif_title) -> isProActive
                                else -> true
                            },
                            isProOnly = when (menu.leadingTitle) {
                                context.getString(R.string.quick_download_notif_title) -> true
                                else -> false
                            },
                            trailingType = when (menu.leadingTitle) {
                                context.getString(R.string.pip) -> Switch(isPipEnabled)
                                context.getString(R.string.quick_download_notif_title) -> Switch(
                                    switchState = isProActive && isQuickNotifEnabled
                                )
                                else -> Default(trailingTitle = trailingTitle)
                            }
                        )
                    }

                    MeverMenuItem(
                        modifier = Modifier.padding(horizontal = Dp24),
                        menuArgs = menuArgs
                    ) {
                        handleClickMenu(
                            context = context,
                            title = menu.leadingTitle,
                            languageCode = getLanguageCode,
                            themeType = themeType,
                            onClickChangeLanguage = { onClickChangeLanguage(it) },
                            onClickNotificationPermission = { onClickNotificationPermission() },
                            onClickChangeTheme = { onClickChangeTheme(it) },
                            onClickPip = { onClickPip() },
                            onClickQuickNotif = { onClickQuickNotif() },
                            onClickCleanCache = { onClickCleanCache() },
                            onClickPaypal = { onClickPaypal() },
                            onClickQris = { onClickQris() },
                            onClickFaq = { onClickFaq() },
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

private fun handleClickMenu(
    context: Context,
    title: String,
    languageCode: String,
    themeType: ThemeType,
    onClickChangeLanguage: (String) -> Unit,
    onClickNotificationPermission: () -> Unit,
    onClickChangeTheme: (ThemeType) -> Unit,
    onClickPip: () -> Unit,
    onClickQuickNotif: () -> Unit,
    onClickCleanCache: () -> Unit,
    onClickPaypal: () -> Unit,
    onClickQris: () -> Unit,
    onClickFaq: () -> Unit,
    onClickContact: () -> Unit,
    onClickAbout: () -> Unit
) = with(context) {
    when (title) {
        getString(R.string.language) -> onClickChangeLanguage(languageCode)
        getString(R.string.notification) -> onClickNotificationPermission()
        getString(R.string.theme) -> onClickChangeTheme(themeType)
        getString(R.string.pip) -> onClickPip()
        getString(R.string.quick_download_notif_title) -> onClickQuickNotif()
        getString(R.string.clean_cache) -> onClickCleanCache()
        getString(R.string.paypal) -> onClickPaypal()
        getString(R.string.qris) -> onClickQris()
        getString(R.string.faq) -> onClickFaq()
        getString(R.string.contact) -> onClickContact()
        getString(R.string.about) -> onClickAbout()
    }
}

@Composable
private fun SettingShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = Dp64)
            .fadingEdge(
                side = Bottom,
                isVisible = true
            )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(Dp16))
            Box(
                modifier = Modifier
                    .padding(horizontal = Dp24)
                    .clip(RoundedCornerShape(Dp8))
                    .meverShimmer(true)
            ) {
                Text(
                    text = stringResource(R.string.settings),
                    style = typography.h2.copy(fontSize = Sp32),
                    color = colors.blackWhite.copy(alpha = 0f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = Dp24, end = Dp24, top = Dp24, bottom = Dp32)
                .height(Dp62)
                .clip(RoundedCornerShape(Dp16))
                .meverShimmer(true)
        )
        repeat(3) { sectionIndex ->
            Box(
                modifier = Modifier
                    .padding(start = Dp24, end = Dp24, bottom = Dp12)
                    .width(if (sectionIndex == 0) Dp80 else if (sectionIndex == 1) Dp100 else Dp60)
                    .height(Dp20)
                    .clip(RoundedCornerShape(Dp4))
                    .meverShimmer(true)
            )
            repeat(if (sectionIndex == 0) 3 else if (sectionIndex == 1) 4 else 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dp24, vertical = Dp12),
                    horizontalArrangement = SpaceBetween,
                    verticalAlignment = CenterVertically
                ) {
                    Row(
                        horizontalArrangement = spacedBy(Dp16),
                        verticalAlignment = CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(Dp40)
                                .clip(CircleShape)
                                .meverShimmer(true)
                        )
                        Box(
                            modifier = Modifier
                                .width(Dp130)
                                .height(Dp16)
                                .clip(RoundedCornerShape(Dp4))
                                .meverShimmer(true)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(Dp40)
                            .clip(RoundedCornerShape(Dp8))
                            .meverShimmer(true)
                    )
                }
            }
            Spacer(modifier = Modifier.height(Dp28))
        }
    }
}
