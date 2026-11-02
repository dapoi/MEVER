package com.dapascript.mever.feature.setting.screen

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverIcon
import com.dapascript.mever.core.common.ui.component.MeverPermissionHandler
import com.dapascript.mever.core.common.ui.component.MeverSwitch
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp40
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverViolet
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.getNotificationPermission
import com.dapascript.mever.core.common.util.navigateToNotificationSettings
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.cancelQuickDownloadNotification
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.showQuickDownloadNotification
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.helper.Navigator
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingPaywallRoute
import com.dapascript.mever.feature.setting.viewmodel.SettingQuickDownloadViewModel

@Composable
internal fun SettingQuickDownloadScreen(
    navigator: Navigator,
    viewModel: SettingQuickDownloadViewModel = hiltViewModel()
) = with(viewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isQuickNotifEnabled = isQuickNotifEnabled.collectAsStateValue()
    val isProActive = isProActive.collectAsStateValue()
    var checkPermissions by remember { mutableStateOf<List<String>>(emptyList()) }
    var pendingEnableQuickNotif by remember { mutableStateOf(value = false) }

    BaseScreen(
        topBarArgs = TopBarArgs(title = stringResource(R.string.quick_download_notif_title)),
        onBackHandler = { navigator.navigateBack() }
    ) {
        if (checkPermissions.isNotEmpty()) {
            MeverPermissionHandler(
                permissions = checkPermissions,
                onGranted = {
                    checkPermissions = emptyList()
                    if (pendingEnableQuickNotif) {
                        saveQuickNotifState(true)
                        showQuickDownloadNotification(context)
                        pendingEnableQuickNotif = false
                    }
                },
                onDenied = { isPermanentlyDeclined, onRetry ->
                    pendingEnableQuickNotif = false
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = Dp64)
                .verticalScroll(scrollState)
                .padding(horizontal = Dp24, vertical = Dp16),
            verticalArrangement = spacedBy(Dp20)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dp20))
                    .background(colors.whiteDarkGray)
                    .border(
                        width = Dp1,
                        color = colors.blackWhite.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(Dp20)
                    )
                    .padding(Dp16)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = spacedBy(Dp12)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = spacedBy(Dp14),
                            verticalAlignment = CenterVertically
                        ) {
                            MeverIcon(
                                icon = R.drawable.ic_quick_download_notif,
                                iconBackgroundColor = MeverPink,
                                iconSize = Dp40,
                                iconPadding = Dp8
                            )
                            Column {
                                Text(
                                    text = stringResource(R.string.quick_download_notif_title),
                                    style = typography.bodyBold1,
                                    color = colors.blackWhite
                                )
                                Text(
                                    text = stringResource(R.string.quick_download_setting_desc),
                                    style = typography.body2,
                                    color = colors.blackWhite.copy(alpha = 0.6f)
                                )
                            }
                        }
                        MeverSwitch(
                            isChecked = isQuickNotifEnabled,
                            onCheckedChange = { isChecked ->
                                if (!isProActive) {
                                    navigator.navigate(SettingPaywallRoute)
                                    return@MeverSwitch
                                }
                                if (isChecked) {
                                    val notifPermission = getNotificationPermission()
                                    if (notifPermission.any { context.checkSelfPermission(it) != PERMISSION_GRANTED }) {
                                        pendingEnableQuickNotif = true
                                        checkPermissions = notifPermission
                                    } else {
                                        saveQuickNotifState(true)
                                        showQuickDownloadNotification(context)
                                    }
                                } else {
                                    saveQuickNotifState(false)
                                    cancelQuickDownloadNotification(context)
                                }
                            }
                        )
                    }

                    Text(
                        text = stringResource(R.string.quick_download_notif_section_desc),
                        style = typography.body2,
                        color = colors.blackWhite.copy(alpha = 0.7f)
                    )
                }
            }
            if (isProActive) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dp20))
                        .background(colors.whiteDarkGray)
                        .border(
                            width = Dp1,
                            color = colors.blackWhite.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(Dp20)
                        )
                        .padding(Dp16)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = spacedBy(Dp14)
                    ) {
                        Row(
                            horizontalArrangement = spacedBy(Dp14),
                            verticalAlignment = CenterVertically
                        ) {
                            MeverIcon(
                                icon = R.drawable.ic_quick_tile,
                                iconBackgroundColor = MeverViolet,
                                iconSize = Dp40,
                                iconPadding = Dp8
                            )
                            Column {
                                Text(
                                    text = stringResource(R.string.quick_tile_section_title),
                                    style = typography.bodyBold1,
                                    color = colors.blackWhite
                                )
                                Text(
                                    text = stringResource(R.string.quick_tile_how_to_use),
                                    style = typography.body2,
                                    color = colors.blackWhite.copy(alpha = 0.6f)
                                )
                            }
                        }
                        Text(
                            text = stringResource(R.string.quick_tile_section_desc),
                            style = typography.body2,
                            color = colors.blackWhite.copy(alpha = 0.7f)
                        )
                        val steps = listOf(
                            R.string.quick_tile_step_1,
                            R.string.quick_tile_step_2,
                            R.string.quick_tile_step_3,
                            R.string.quick_tile_step_4,
                            R.string.quick_tile_step_5
                        )
                        steps.forEachIndexed { index, stepResId ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = spacedBy(Dp12),
                                verticalAlignment = Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(Dp20)
                                        .clip(CircleShape)
                                        .background(MeverPurple),
                                    contentAlignment = Center
                                ) {
                                    Text(
                                        text = (index + 1).toString(),
                                        style = typography.labelBold2,
                                        color = MeverWhite
                                    )
                                }
                                Text(
                                    text = stringResource(stepResId),
                                    style = typography.body2,
                                    color = colors.blackWhite.copy(alpha = 0.85f),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(Dp32))
        }
    }
}