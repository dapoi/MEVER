package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.clickableSingle

@Composable
fun MeverLabel(
    message: String,
    actionMessage: String,
    modifier: Modifier = Modifier,
    onActionLabelClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MeverPurple, shape = RoundedCornerShape(Dp8))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp12),
            horizontalArrangement = SpaceBetween
        ) {
            Text(
                text = message,
                style = typography.label1,
                color = MeverWhite
            )
            Text(
                modifier = Modifier.clickableSingle { onActionLabelClick() },
                text = actionMessage,
                style = typography.labelBold1,
                color = MeverWhite
            )
        }
    }
}