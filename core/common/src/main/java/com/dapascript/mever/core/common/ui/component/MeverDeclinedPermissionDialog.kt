package com.dapascript.mever.core.common.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dapascript.mever.core.common.R

@Composable
fun MeverDeclinedPermissionDialog(
    isPermissionsDeclined: Boolean,
    onGoToSetting: () -> Unit,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    MeverDialog(
        showDialog = true,
        title = stringResource(R.string.permission_request_title),
        description = stringResource(R.string.permission_request_media),
        primaryActionLabel = stringResource(
            if (isPermissionsDeclined) R.string.go_to_settings else R.string.allow
        ),
        onClickPrimaryAction = if (isPermissionsDeclined) onGoToSetting else onRetry,
        onClickSecondaryAction = onDismiss
    )
}