package com.dapascript.mever.feature.home.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography

@Composable
internal fun HandleDialogError(
    showDialog: Boolean,
    errorTitle: String,
    errorDescription: String,
    errorImage: Int = R.drawable.ic_error_response,
    primaryButtonText: String = stringResource(R.string.retry),
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = errorTitle,
            primaryButtonText = primaryButtonText,
            onClickPrimaryButton = onRetry,
            onClickSecondaryButton = onDismiss
        )
    ) {
        Image(
            modifier = Modifier
                .size(Dp200)
                .align(CenterHorizontally),
            painter = painterResource(errorImage),
            contentScale = Crop,
            contentDescription = "Error Image"
        )
        Text(
            text = errorDescription,
            textAlign = Center,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}