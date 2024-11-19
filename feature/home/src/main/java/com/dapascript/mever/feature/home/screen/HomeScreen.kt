package com.dapascript.mever.feature.home.screen

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.util.Patterns.WEB_URL
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.dapascript.mever.core.common.ui.component.MeverBottomSheet
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverDownloadButton
import com.dapascript.mever.core.common.ui.component.MeverTextField
import com.dapascript.mever.core.common.ui.component.PermissionDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp32
import com.dapascript.mever.core.common.util.Constant.ScreenName.EXPLORE
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.getStoragePermission
import com.dapascript.mever.core.common.util.goToSetting
import com.dapascript.mever.feature.home.screen.attr.HomeScreenAttr.listOfActionMenu
import com.dapascript.mever.feature.home.viewmodel.HomeViewModel
import com.ketch.Ketch

@Composable
fun HomeScreen(
    navigator: BaseNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) = with(viewModel) {
    val videoState = videoState.collectAsStateValue()
    val activity = LocalActivity.current
    val dialogQueue = showDialogPermission
    var id by remember { mutableIntStateOf(0) }
    var urlContent by remember { mutableStateOf<String?>(null) }
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
        listMenuAction = listOfActionMenu.map { (name, resource) ->
            ActionMenu(resource = resource, name = name, isShowBadge = name == NOTIFICATION)
        },
        onClickActionMenu = { name -> onClickActionMenu(name) }
    ) {
        Column(
            modifier = Modifier.padding(top = Dp32),
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
                    enabled = urlSocialMediaState.text.trim().isValidUrl(),
                    isLoading = isLoading
                ) { if (isLoading.not()) requestStoragePermissionLauncher.launch(getStoragePermission) }
            }
        }
        dialogQueue.reversed().forEach { permission ->
            MeverDialog(
                showDialog = true,
                onDismiss = ::dismissDialog
            ) {
                PermissionDialog(
                    isPermissionsDeclined = shouldShowRequestPermissionRationale(activity, permission).not(),
                    descriptionPermission = getDescriptionPermission(permission),
                    onGoToSetting = {
                        dismissDialog()
                        activity.goToSetting()
                    },
                    onAllow = {
                        dismissDialog()
                        requestStoragePermissionLauncher.launch(arrayOf(permission))
                    },
                    onDismiss = ::dismissDialog,
                )
            }
        }

        LaunchedEffect(videoState) {
            videoState.handleUiState(
                onLoading = { isLoading = true },
                onSuccess = {
                    isLoading = false
                    urlContent = it.first().url
                },
                onFailed = { throwable ->
                    isLoading = false
                    Log.e("HomeScreen", "getApiDownloader: $throwable")
                }
            )
        }

        LaunchedEffect(urlContent) {
            urlContent?.let {
                ketch.observeDownloads().collect { model ->
                    Log.d("HomeScreen", "Download: $model")
                }
            }
        }

        urlContent?.let { url ->
            MeverBottomSheet(
                onClick = {
                    resetState()
                    id = downloadFile(ketch, url)
                    urlContent = null
                },
                onDismiss = {
                    resetState()
                    urlContent = null
                }
            )
        }
    }
}

private fun downloadFile(ketch: Ketch, url: String) = ketch.download(
    url = url,
    path = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path,
    fileName = "mever_${System.currentTimeMillis()}"
)

private fun getActionMenuClick(navigator: BaseNavigator) = { name: String ->
    when (name) {
        NOTIFICATION -> navigator.run { navigate(getNavGraph<NotificationNavGraph>().getNotificationRoute()) }
        EXPLORE -> navigator.run {}
        SETTING -> navigator.run { navigate(getNavGraph<SettingNavGraph>().getSettingRoute()) }
        else -> Unit
    }
}

private fun String.isValidUrl() = WEB_URL.matcher(this).matches()

private fun getDescriptionPermission(permission: String) = when (permission) {
    READ_MEDIA_IMAGES -> "We need to access your images to download the file"
    READ_MEDIA_VIDEO -> "We need to access your videos to download the file"
    else -> "We need to access your storage to download the file"
}