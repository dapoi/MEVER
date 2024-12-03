package com.dapascript.mever.feature.home.screen

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.base.attr.BaseScreenAttr.ActionMenu
import com.dapascript.mever.core.common.base.attr.BaseScreenAttr.BaseScreenArgs
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.GalleryNavGraph
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverDownloadButton
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.MeverThumbnail
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.ScreenName.GALLERY
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.getDescriptionPermission
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.common.util.isValidUrl
import com.dapascript.mever.core.model.local.VideoGeneralEntity
import com.dapascript.mever.feature.home.screen.attr.HomeScreenAttr.DownloaderArgs
import com.dapascript.mever.feature.home.screen.attr.HomeScreenAttr.listOfActionMenu
import com.dapascript.mever.feature.home.viewmodel.HomeLandingViewModel

@Composable
internal fun HomeLandingScreen(
    navigator: BaseNavigator,
    viewModel: HomeLandingViewModel = hiltViewModel()
) = with(viewModel) {
    val videoState = videoState.collectAsStateValue()
    val activity = LocalActivity.current
    val dialogQueue = showDialogPermission
    var listVideo by remember { mutableStateOf<List<VideoGeneralEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val onClickActionMenu = remember { getActionMenuClick(navigator) }
    val requestStoragePermissionLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) { perms ->
        val allGranted = getStoragePermission.all { perms[it] == true }
        if (allGranted) getApiDownloader(urlSocialMediaState)
        else getStoragePermission.forEach { permission ->
            onPermissionResult(permission, isGranted = perms[permission] == true)
        }
    }

    BaseScreen(
        baseScreenArgs = BaseScreenArgs(
            screenName = null,
            actionMenus = listOfActionMenu.map { (name, resource) ->
                ActionMenu(
                    icon = resource,
                    nameIcon = name,
                    showBadge = showBadge && name == NOTIFICATION,
                ) { onClickActionMenu(name) }
            }
        )
    ) {
        LaunchedEffect(Unit) { getObservableKetch() }

        LaunchedEffect(videoState) {
            videoState.handleUiState(
                onLoading = { isLoading = true },
                onSuccess = {
                    isLoading = false
                    listVideo = it
                },
                onFailed = {
                    isLoading = false
                    isError = true
                }
            )
        }

        HandleDialogError(
            showDialog = isError,
            onRetry = {
                getApiDownloader(urlSocialMediaState)
                isError = false
            },
            onDismiss = { isError = false }
        )

        HandleDialogPermission(
            activity = activity,
            dialogQueue = dialogQueue,
            onGoToSetting = {
                dismissDialog()
                activity.goToSetting()
            },
            onAllow = {
                dismissDialog()
                requestStoragePermissionLauncher.launch(getStoragePermission)
            },
            onDismiss = ::dismissDialog
        )

        HandleDialogDownload(
            downloadArgs = listVideo.map {
                DownloaderArgs(
                    url = it.url,
                    quality = it.quality
                )
            },
            showDialog = listVideo.isNotEmpty(),
            onDownloadClick = { url ->
                downloadFile(
                    url = url,
                    platformName = urlSocialMediaState.text.getPlatformType().platformName
                )
                listVideo = emptyList()
                urlSocialMediaState = urlSocialMediaState.copy(text = "")
                resetState()
            },
            onDismiss = {
                listVideo = emptyList()
                resetState()
            }
        )

        HomeScreenContent(
            homeLandingViewModel = this,
            isLoading = isLoading
        ) { requestStoragePermissionLauncher.launch(getStoragePermission) }
    }
}

@Composable
private fun HandleDialogError(
    showDialog: Boolean,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = "Ups, something went wrong!",
            actionText = "Retry",
            onActionClick = onRetry,
            onDismissClick = onDismiss
        )
    ) {
        Text(
            text = "Please press the button below to try again",
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}

@Composable
private fun HandleDialogPermission(
    activity: Activity,
    dialogQueue: List<String>,
    onGoToSetting: () -> Unit,
    onAllow: () -> Unit,
    onDismiss: () -> Unit
) {
    dialogQueue.reversed().forEach { permission ->
        val isPermissionsDeclined = shouldShowRequestPermissionRationale(activity, permission).not()

        MeverDialog(
            showDialog = true,
            meverDialogArgs = MeverDialogArgs(
                title = "Permission required",
                actionText = if (isPermissionsDeclined) "Go to setting" else "Allow",
                onActionClick = if (isPermissionsDeclined) onGoToSetting else onAllow,
                onDismissClick = onDismiss
            )
        ) {
            Text(
                text = getDescriptionPermission(permission),
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun HandleDialogDownload(
    downloadArgs: List<DownloaderArgs>,
    showDialog: Boolean,
    onDownloadClick: (String) -> Unit,
    onDismiss: () -> Unit
) = with(downloadArgs) {
    var chooseQuality by remember(this) { mutableStateOf(firstOrNull { it.quality.isNotEmpty() }?.url) }

    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = "The video is ready to download",
            actionText = "Download",
            onActionClick = { onDownloadClick(chooseQuality ?: get(0).url) },
            onDismissClick = onDismiss
        )
    ) {
        MeverThumbnail(
            source = size.takeIf { it > 0 }?.let { get(0).url } ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(Dp8))
        )
        filter { it.quality.isNotEmpty() && it.url.isValidUrl() }.map { (url, quality) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dp4))
                    .clickableSingle { chooseQuality = url },
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = quality,
                    style = typography.body1,
                    color = colorScheme.onPrimary
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp0) {
                    RadioButton(
                        modifier = Modifier.padding(vertical = Dp4),
                        colors = colors(selectedColor = MeverPurple, unselectedColor = colorScheme.onPrimary),
                        selected = chooseQuality == url,
                        onClick = null
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    homeLandingViewModel: HomeLandingViewModel,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    requestStoragePermissionLauncher: () -> Unit
) = with(homeLandingViewModel) {
    Column(
        modifier = modifier
            .padding(top = Dp32)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = spacedBy(Dp16)
        ) {
            MeverTextField(
                modifier = Modifier.weight(1f),
                webDomainValue = urlSocialMediaState,
                onValueChange = { urlSocialMediaState = it }
            )
            MeverDownloadButton(
                enabled = urlSocialMediaState.text.trim().getPlatformType() != UNKNOWN,
                isLoading = isLoading
            ) { if (isLoading.not()) requestStoragePermissionLauncher() }
        }
    }
}

private fun getActionMenuClick(navigator: BaseNavigator) = { name: String ->
    when (name) {
        NOTIFICATION -> navigator.navigateToNotif()
        GALLERY -> navigator.run { navigate(getNavGraph<GalleryNavGraph>().getGalleryLandingRoute()) }
        SETTING -> navigator.run { navigate(getNavGraph<SettingNavGraph>().getSettingLandingRoute()) }
        else -> Unit
    }
}

private fun BaseNavigator.navigateToNotif() = navigate(getNavGraph<NotificationNavGraph>().getNotificationLandingRoute())