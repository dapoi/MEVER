package com.dapascript.mever.feature.home.screen.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.component.MeverRadioButton

@Composable
internal fun HandleDialogYoutubeQuality(
    showDialog: Boolean,
    onApplyQuality: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val qualityList = remember { listOf("360p", "480p", "720p", "1080p") }
    var chooseQuality by remember { mutableStateOf(qualityList[0]) }

    MeverDialog(
        showDialog = showDialog,
        meverDialogArgs = MeverDialogArgs(
            title = stringResource(R.string.choose_quality),
            primaryButtonText = stringResource(R.string.apply),
            secondaryButtonText = stringResource(R.string.cancel),
            onClickPrimaryButton = {
                onApplyQuality(chooseQuality)
                onDismiss()
            },
            onClickSecondaryButton = onDismiss
        )
    ) {
        qualityList.map { quality ->
            MeverRadioButton(
                value = quality,
                isChoosen = chooseQuality == quality
            ) { chooseQuality = quality }
        }
    }
}