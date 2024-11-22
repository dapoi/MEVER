package com.dapascript.mever.feature.home.screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.base.BaseScreen
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.ui.attr.ActionMenuAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverDownloadAttr.MeverDownloadArgs
import com.dapascript.mever.core.common.ui.component.DownloadDialog
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverDownloadButton
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.PermissionDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.ScreenName.EXPLORE
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.downloadFile
import com.dapascript.mever.core.common.util.getDescriptionPermission
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.core.model.local.VideoGeneralEntity
import com.dapascript.mever.feature.home.screen.attr.HomeScreenAttr.listOfActionMenu
import com.dapascript.mever.feature.home.viewmodel.HomeViewModel

@Composable
internal fun HomeScreen(
    navigator: BaseNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) = with(viewModel) {
    val videoState = videoState.collectAsStateValue()
    val activity = LocalActivity.current
    val dialogQueue = showDialogPermission
    var listVideo by remember { mutableStateOf<List<VideoGeneralEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val onClickActionMenu = remember { getActionMenuClick(navigator) }
    val requestStoragePermissionLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) { perms ->
        getStoragePermission.forEach { permission ->
            onPermissionResult(
                permission = permission,
                isGranted = perms[permission] == true
            ) { getApiDownloader(urlSocialMediaState) }
        }
    }

    BaseScreen(
        actionMenus = listOfActionMenu.map { (name, resource) ->
            ActionMenu(
                resource = resource,
                name = name,
                showBadge = showBadge
            )
        },
        onClickActionMenu = { name -> onClickActionMenu(name) }
    ) {
        LaunchedEffect(Unit) { getObservableKetch() }

        LaunchedEffect(videoState) {
            videoState.handleUiState(
                onLoading = { isLoading = true },
                onSuccess = {
                    isLoading = false
                    listVideo = it
                },
                onFailed = { throwable ->
                    isLoading = false
                    Log.e("HomeScreen", "getApiDownloader: $throwable")
                }
            )
        }

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
            listVideo = listVideo,
            showDialog = listVideo.isNotEmpty(),
            onDownloadClick = { url ->
                Log.d("HomeScreen", "$url ")
                downloadFile(ketch, url)
                listVideo = emptyList()
                resetState()
            },
            onDismiss = {
                listVideo = emptyList()
                resetState()
            }
        )

        HomeScreenContent(
            homeViewModel = this,
            isLoading = isLoading
        ) { requestStoragePermissionLauncher.launch(getStoragePermission) }
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
        MeverDialog(
            showDialog = true,
            onDismiss = onDismiss
        ) {
            PermissionDialog(
                isPermissionsDeclined = shouldShowRequestPermissionRationale(activity, permission).not(),
                descriptionPermission = getDescriptionPermission(permission),
                onGoToSetting = onGoToSetting,
                onAllow = onAllow,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun HandleDialogDownload(
    listVideo: List<VideoGeneralEntity>,
    showDialog: Boolean,
    onDownloadClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    MeverDialog(
        showDialog = showDialog,
        onDismiss = onDismiss
    ) {
        DownloadDialog(
            meverDownloadArgs = listVideo.map {
                MeverDownloadArgs(
                    url = it.url,
                    quality = it.quality
                )
            },
            onDownloadClick = { onDownloadClick(it) },
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun HomeScreenContent(
    homeViewModel: HomeViewModel,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    requestStoragePermissionLauncher: () -> Unit
) = with(homeViewModel) {
    Column(
        modifier = modifier.padding(top = Dp32),
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
        NOTIFICATION -> navigator.run { navigate(getNavGraph<NotificationNavGraph>().getNotificationRoute()) }
        EXPLORE -> navigator.run {}
        SETTING -> navigator.run { navigate(getNavGraph<SettingNavGraph>().getSettingRoute()) }
        else -> Unit
    }
}