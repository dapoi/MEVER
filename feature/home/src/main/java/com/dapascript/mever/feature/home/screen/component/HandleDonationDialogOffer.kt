package com.dapascript.mever.feature.home.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import androidx.compose.ui.Alignment.Companion.Center as AlignmentCenter

@Composable
internal fun HandleDonationDialogOffer(
    showDialog: Boolean,
    onClickPrimaryButton: () -> Unit,
    onClickSecondaryButton: () -> Unit
) {
    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = stringResource(R.string.thanks),
            primaryButtonText = stringResource(R.string.sure),
            secondaryButtonText = stringResource(R.string.later),
            onClickPrimaryButton = onClickPrimaryButton,
            onClickSecondaryButton = onClickSecondaryButton
        )
    ) {
        Box(
            modifier = Modifier
                .size(Dp200)
                .align(CenterHorizontally),
            contentAlignment = AlignmentCenter
        ) {
            Image(
                modifier = Modifier.size(Dp150),
                painter = painterResource(R.drawable.ic_coffee),
                contentScale = FillBounds,
                contentDescription = "Donation Icon"
            )
        }
        Text(
            text = stringResource(R.string.donation_desc),
            textAlign = Center,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
    }
}