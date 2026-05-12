package com.dapascript.mever.feature.setting.screen.component

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverDialog
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
        image = null,
        title = title,
        description = address,
        primaryActionLabel = stringResource(R.string.copy),
        onClickPrimaryAction = {
            copyToClipboard(context, address)
            onDismissDialog(null)
        },
        onClickSecondaryAction = { onDismissDialog(null) }
    )
}

private fun getContentDonateTypeDialog(
    context: Context,
    donateType: AppreciateType
) = when (donateType) {
    BITCOIN -> Pair(context.getString(R.string.bitcoin_address), BTC_ADDRESS)
    PAYPAL -> Pair(context.getString(R.string.paypal_email), PAYPAL_EMAIL)
}