package com.dapascript.mever.feature.home.screen.component

import android.app.Activity
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.getDescriptionPermission

@Composable
internal fun HandleDialogPermission(
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
                title = "Permission Required",
                primaryButtonText = if (isPermissionsDeclined) "Go to setting" else "Allow",
                onClickAction = if (isPermissionsDeclined) onGoToSetting else onAllow,
                onDismiss = onDismiss
            )
        ) {
            Text(
                text = getDescriptionPermission(permission),
                textAlign = Center,
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }
    }
}