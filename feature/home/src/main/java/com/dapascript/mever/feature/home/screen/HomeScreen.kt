package com.dapascript.mever.feature.home.screen

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.app.Activity
import android.content.Intent
import android.net.Uri.fromParts
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.hilt.navigation.compose.hiltViewModel
import com.dapascript.mever.core.common.navigation.base.BaseNavigator
import com.dapascript.mever.core.common.navigation.graph.NotificationNavGraph
import com.dapascript.mever.core.common.navigation.graph.SettingNavGraph
import com.dapascript.mever.core.common.ui.attr.ActionMenuAttr.ActionMenu
import com.dapascript.mever.core.common.ui.base.BaseScreen
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
import com.dapascript.mever.core.common.util.isValidUrl
import com.dapascript.mever.feature.home.R
import com.dapascript.mever.feature.home.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navigator: BaseNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) = with(viewModel) {
    val activity = LocalActivity.current
    val dialogQueue = showDialogPermission
    val requestNotificationPermissionLauncher = rememberLauncherForActivityResult(RequestPermission()) {}
    val requestStoragePermissionLauncher = rememberLauncherForActivityResult(RequestMultiplePermissions()) { perms ->
        getStoragePermission.forEach { permission ->
            onPermissionResult(
                permission = permission,
                isGranted = perms[permission] == true
            ) {}
        }
    }
    val onClickActionMenu = remember { getActionMenuClick(navigator) }
    val listOfActionMenu = mapOf(
        NOTIFICATION to R.drawable.ic_notification,
        EXPLORE to R.drawable.ic_explore,
        SETTING to R.drawable.ic_setting
    )

    BaseScreen(
        screenName = "",
        isHome = true,
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
                    webDomainValue = domain,
                    onValueChange = { domain = it }
                )
                MeverDownloadButton(enabled = domain.text.isValidUrl()) {
                    requestStoragePermissionLauncher.launch(getStoragePermission)
                }
            }
        }
    }

    dialogQueue.reversed().forEach { permission ->
        MeverDialog(
            showDialog = true,
            onDismiss = ::dismissDialog
        ) {
            PermissionDialog(
                isPermissionsDeclined = shouldShowRequestPermissionRationale(activity, permission).not(),
                descriptionPermission = when (permission) {
                    READ_MEDIA_IMAGES -> "We need to access your images to download the file"
                    READ_MEDIA_VIDEO -> "We need to access your videos to download the file"
                    else -> "We need to access your storage to download the file"
                },
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
}

private fun getActionMenuClick(navigator: BaseNavigator) = { name: String ->
    when (name) {
        NOTIFICATION -> navigator.run { navigate(getNavGraph<NotificationNavGraph>().getNotificationRoute()) }
        EXPLORE -> navigator.run {}
        SETTING -> navigator.run { navigate(getNavGraph<SettingNavGraph>().getSettingRoute()) }
        else -> Unit
    }
}

private fun Activity.goToSetting() {
    Intent(
        ACTION_APPLICATION_DETAILS_SETTINGS,
        fromParts("package", packageName, null)
    ).also(::startActivity)
}