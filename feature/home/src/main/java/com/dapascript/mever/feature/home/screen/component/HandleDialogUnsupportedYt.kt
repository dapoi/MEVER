package com.dapascript.mever.feature.home.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
internal fun HandleDialogUnsupportedYt(
    showUnsupportedYouTubeDialog: Boolean,
    onClick: () -> Unit
) {
    MeverDialog(
        showDialog = showUnsupportedYouTubeDialog,
        meverDialogArgs = MeverDialogArgs(
            title = stringResource(R.string.error_title)
        ),
        hideInteractionButton = true
    ) {
        Image(
            modifier = Modifier
                .size(Dp200)
                .align(CenterHorizontally),
            painter = painterResource(R.drawable.ic_error),
            contentScale = Crop,
            contentDescription = "Maintenance Image"
        )
        Text(
            text = stringResource(R.string.unsupported_yt),
            textAlign = TextAlign.Center,
            style = typography.body1,
            color = colorScheme.onPrimary
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .onCustomClick { onClick() }
                .padding(vertical = Dp8),
            text = stringResource(R.string.ok),
            textAlign = TextAlign.Center,
            style = typography.bodyBold1,
            color = colorScheme.primary
        )
    }
}