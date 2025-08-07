package com.dapascript.mever.core.common.ui.component

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography

@Composable
fun MeverDeclinedPermission(
    isPermissionsDeclined: Boolean,
    onGoToSetting: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    MeverDialog(
        showDialog = true,
        meverDialogArgs = MeverDialogArgs(
            title = stringResource(R.string.permission_request_title),
            primaryButtonText = stringResource(
                if (isPermissionsDeclined) R.string.go_to_settings else R.string.allow
            ),
            onClickPrimaryButton = if (isPermissionsDeclined) onGoToSetting else onRetry,
            onClickSecondaryButton = onDismiss
        )
    ) {
        Text(
            text = stringResource(R.string.permission_request_media),
            textAlign = Center,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}