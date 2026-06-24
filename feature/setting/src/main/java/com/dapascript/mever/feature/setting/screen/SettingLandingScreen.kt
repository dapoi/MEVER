package com.dapascript.mever.feature.setting.screen

import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.text.format.Formatter
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp90
import com.dapascript.mever.core.common.ui.theme.MeverOrange
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverRed
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp20
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp22
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.DeviceType
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.DeviceType.TABLET
import com.dapascript.mever.core.common.util.LanguageManager.getLanguageCode
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.cleanCache
import com.dapascript.mever.core.common.util.copyToClipboard
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.navigateToGmail
import com.dapascript.mever.core.common.util.navigateToNotificationSettings
import com.dapascript.mever.core.common.util.recreateActivity
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.common.util.storage.StorageUtil.StorageInfo
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingAboutAppRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingFaqRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import com.dapascript.mever.feature.setting.screen.component.HandleBottomSheetQris
import com.dapascript.mever.feature.setting.viewmodel.SettingLandingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun SettingLandingScreen(
    navigator: Navigator,
    viewModel: SettingLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val themeType = themeType.collectAsStateValue()
    val isPipEnabled = isPipEnabled.collectAsStateValue()
    val storageInfo = storageInfo.collectAsStateValue()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val resources = LocalResources.current
    val deviceType = LocalDeviceType.current
    val listState = rememberLazyListState()
    val statusColor = remember(storageInfo?.usedPercent) {
        getStatusStorageColor(storageInfo?.usedPercent ?: 0)
    }
    var titleHeight by rememberSaveable { mutableIntStateOf(0) }
    var showPaypalDialog by remember { mutableStateOf(false) }
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
            onClickBack = { navigator.goBack() }
        )
    ) {
        LaunchedEffect(listState, titleHeight) {
            delay(1.seconds)
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

        LaunchedEffect(storageInfo?.usedPercent) {
            delay(350.milliseconds)
            storageInfo?.usedPercent?.let {
                animatedPercent = storageInfo.usedPercent / 100f
            }
        }

        LaunchedEffect(context) { languageCode = getLanguageCode(context) }

        if (setRequestPermission.isNotEmpty()) {
            MeverPermissionHandler(
                permissions = setRequestPermission,
                onGranted = { setRequestPermission = emptyList() },
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
                                setRequestPermission = emptyList()
                                navigateToNotificationSettings(context)
                            } else onRetry()
                        },
                        onClickSecondaryAction = { setRequestPermission = emptyList() }
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

        HandleBottomSheetQris(showBottomSheetQris) { showBottomSheetQris = it }

        SettingLandingContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64),
            context = context,
            titleHeight = titleHeight,
            usedStorage = usedStorage,
            statusColor = statusColor,
            deviceType = deviceType,
            listState = listState,
            isExpanded = isExpanded,
            isPipEnabled = isPipEnabled,
            getLanguageCode = languageCode,
            themeType = themeType,
            storageInfo = storageInfo,
            onClickChangeLanguage = { languageCode ->
                navigator.navigate(SettingLanguageRoute(languageCode))
            },
            onClickNotificationPermission = {
                val perm = getNotificationPermission().firstOrNull()
                if (perm != null && context.checkSelfPermission(perm) != PERMISSION_GRANTED) {
                    setRequestPermission = listOf(perm)
                } else navigateToNotificationSettings(context)
            },
            onClickChangeTheme = { navigator.navigate(SettingScreenRoute.SettingThemeRoute(it)) },
            onClickCleanCache = {
                cleanCache(context)
                recreateActivity(context, activity)
            },
            onClickPip = { savePipState(isPipEnabled.not()) },
            onClickPaypal = { showPaypalDialog = true },
            onClickQris = { showBottomSheetQris = true },
            onClickFaq = { navigator.navigate(SettingFaqRoute) },
            onClickContact = { navigateToGmail(context) },
            onClickAbout = { navigator.navigate(SettingAboutAppRoute) },
            onSetTitleHeight = { titleHeight = it }
        )
    }
}

@Composable
private fun SettingLandingContent(
    context: Context,
    titleHeight: Int,
    usedStorage: Float,
    statusColor: Color,
    deviceType: DeviceType,
    listState: LazyListState,
    isExpanded: Boolean,
    isPipEnabled: Boolean,
    getLanguageCode: String,
    themeType: ThemeType,
    storageInfo: StorageInfo?,
    modifier: Modifier = Modifier,
    onClickChangeLanguage: (String) -> Unit,
    onClickNotificationPermission: () -> Unit,
    onClickChangeTheme: (ThemeType) -> Unit,
    onClickCleanCache: () -> Unit,
    onClickPip: () -> Unit,
    onClickPaypal: () -> Unit,
    onClickQris: () -> Unit,
    onClickFaq: () -> Unit,
    onClickContact: () -> Unit,
    onClickAbout: () -> Unit,
    onSetTitleHeight: (Int) -> Unit
) = CompositionLocalProvider(LocalOverscrollFactory provides null) {
    Column(modifier = modifier) {
        if (isExpanded.not() && titleHeight > 0) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(Dp3),
                thickness = Dp1,
                color = colors.blackWhite.copy(alpha = 0.12f)
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
                        .onGloballyPositioned { onSetTitleHeight(it.size.height) }
                ) {
                    Spacer(modifier = Modifier.height(Dp16))
                    AnimatedVisibility(
                        visible = isExpanded,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Text(
                            text = stringResource(R.string.settings),
                            style = typography.h2.copy(fontSize = Sp32),
                            color = colors.blackWhite,
                            modifier = Modifier.padding(horizontal = Dp24)
                        )
                    }
                }
            }
            item {
                AnimatedContent(
                    targetState = storageInfo != null,
                    transitionSpec = {
                        (fadeIn() togetherWith fadeOut()).using(SizeTransform(clip = false))
                    }
                ) { completedFetching ->
                    if (completedFetching) AvailableStorageSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp24, vertical = Dp32),
                        context = context,
                        storageInfo = storageInfo!!,
                        usedStorage = usedStorage,
                        statusColor = statusColor,
                        deviceType = deviceType
                    ) else StorageSectionLoading(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dp24, vertical = Dp32),
                        deviceType = deviceType
                    )
                }
            }
            getSettingMenus(context).forEach { (title, menus) ->
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
                    key = { menu -> menu.leadingTitle }
                ) { menu ->
                    MeverMenuItem(
                        modifier = Modifier.padding(horizontal = Dp24),
                        menuArgs = MenuItemArgs(
                            leadingIcon = menu.icon,
                            leadingIconBackground = menu.iconBackgroundColor,
                            leadingTitle = menu.leadingTitle,
                            leadingDesc = menu.leadingDesc,
                            leadingIconSize = Dp40,
                            leadingIconPadding = Dp8,
                            trailingType = if (menu.leadingTitle != stringResource(R.string.pip)) {
                                Default(
                                    trailingTitle = menu.trailingTitle?.let {
                                        when (menu.leadingTitle) {
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
                            title = menu.leadingTitle,
                            languageCode = getLanguageCode,
                            themeType = themeType,
                            onClickChangeLanguage = { onClickChangeLanguage(it) },
                            onClickNotificationPermission = { onClickNotificationPermission() },
                            onClickChangeTheme = { onClickChangeTheme(it) },
                            onClickPip = { onClickPip() },
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
            horizontalArrangement = spacedBy(Dp24)
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier.size(if (deviceType == PHONE) Dp120 else Dp150),
                    progress = { usedStorage },
                    color = statusColor,
                    strokeWidth = Dp8,
                    trackColor = colors.lightGrayDarkGray,
                    strokeCap = Round,
                    gapSize = Dp0
                )
                Text(
                    text = "$usedPercent%",
                    style = typography.body1.copy(fontSize = Sp20),
                    color = colors.blackWhite
                )
            }
            Column(
                modifier = Modifier.then(if (deviceType == PHONE) Modifier.weight(1f) else Modifier),
                verticalArrangement = spacedBy(Dp4)
            ) {
                Text(
                    text = stringResource(R.string.storage),
                    style = typography.bodyBold1,
                    color = statusColor
                )
                Text(
                    text = getUsedOfTotalText(context, this@with),
                    style = typography.body2,
                    color = colors.blackWhite
                )
            }
        }
    }
}

@Composable
private fun StorageSectionLoading(
    deviceType: DeviceType,
    modifier: Modifier = Modifier
) = Box(modifier = modifier) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(Dp24)
    ) {
        Box(
            modifier = Modifier
                .size(if (deviceType == PHONE) Dp120 else Dp150)
                .clip(CircleShape)
                .background(meverShimmer())
        )
        Column(verticalArrangement = spacedBy(Dp4)) {
            Box(
                modifier = Modifier
                    .height(Sp22.run {
                        when (deviceType) {
                            PHONE -> value.dp
                            TABLET -> (value + 2).dp
                            else -> (value + 4).dp
                        }
                    })
                    .width(Dp90)
                    .background(meverShimmer())
            )
            Box(
                modifier = Modifier
                    .height(Sp20.run {
                        when (deviceType) {
                            PHONE -> value.dp
                            TABLET -> (value + 2).dp
                            else -> (value + 4).dp
                        }
                    })
                    .width(Dp150)
                    .background(meverShimmer())
            )
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
        getString(R.string.clean_cache) -> onClickCleanCache()
        getString(R.string.paypal) -> onClickPaypal()
        getString(R.string.qris) -> onClickQris()
        getString(R.string.faq) -> onClickFaq()
        getString(R.string.contact) -> onClickContact()
        getString(R.string.about) -> onClickAbout()
    }
}