package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverRadioButton(
    value: String,
    isChoosen: Boolean,
    modifier: Modifier = Modifier,
    onValueChoose: () -> Unit
) = Row(
    modifier = modifier
        .fillMaxWidth()
        .padding(vertical = Dp16)
        .clip(RoundedCornerShape(Dp8))
        .onCustomClick { onValueChoose() },
    verticalAlignment = CenterVertically,
    horizontalArrangement = spacedBy(Dp16)
) {
    Box(
        modifier = modifier.onCustomClick { onValueChoose() },
        contentAlignment = Center
    ) {
        Icon(
            painter = painterResource(
                if (isChoosen) R.drawable.ic_circle_checked
                else R.drawable.ic_circle_unchecked
            ),
            contentDescription = "Radio button",
            tint = colorScheme.primary
        )
    }
    Text(
        text = value,
        style = typography.body1,
        color = colorScheme.onPrimary
    )
}