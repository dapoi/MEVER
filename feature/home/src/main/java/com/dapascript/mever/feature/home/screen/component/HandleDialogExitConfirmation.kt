package com.dapascript.mever.feature.home.screen.component

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography

@Composable
internal fun HandleDialogExitConfirmation(
    showDialog: Boolean,
    onClickPrimary: () -> Unit,
    onClickSecondary: () -> Unit
) {
    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = stringResource(R.string.cancel_fetch_title),
            onClickPrimaryButton = onClickPrimary,
            onClickSecondaryButton = onClickSecondary
        )
    ) {
        Text(
            text = stringResource(R.string.cancel_fetch_desc),
            textAlign = Center,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}