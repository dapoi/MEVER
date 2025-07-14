package com.dapascript.mever.feature.setting.screen.component

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.copyToClipboard
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType.BITCOIN
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.AppreciateType.PAYPAL
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.BTC_ADDRESS
import com.dapascript.mever.feature.setting.screen.attr.HandleAppreciateDialogAttr.PAYPAL_EMAIL

@Composable
internal fun HandleAppreciateDialog(
    context: Context,
    appreciateType: AppreciateType,
    onDismissDialog: (AppreciateType?) -> Unit
) = getContentDonateTypeDialog(context, appreciateType).let { (title, address) ->
    MeverDialog(
        showDialog = true,
        meverDialogArgs = MeverDialogArgs(
            title = title,
            primaryButtonText = stringResource(R.string.copy),
            onClickPrimaryButton = {
                copyToClipboard(context, address)
                onDismissDialog(null)
            },
            onClickSecondaryButton = { onDismissDialog(null) }
        )
    ) {
        Text(
            text = address,
            textAlign = Center,
            style = typography.body1,
            color = colorScheme.onPrimary,
            modifier = Modifier.padding(vertical = Dp8)
        )
    }
}

private fun getContentDonateTypeDialog(
    context: Context,
    donateType: AppreciateType
) = when (donateType) {
    BITCOIN -> Pair(context.getString(R.string.bitcoin_address), BTC_ADDRESS)
    PAYPAL -> Pair(context.getString(R.string.paypal_email), PAYPAL_EMAIL)
}