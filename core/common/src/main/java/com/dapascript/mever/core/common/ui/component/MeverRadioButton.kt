package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverPurple
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
        .clip(RoundedCornerShape(Dp8))
        .onCustomClick(onClick = { onValueChoose() })
        .padding(vertical = Dp16, horizontal = Dp24),
    verticalAlignment = CenterVertically,
    horizontalArrangement = spacedBy(Dp16)
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .onCustomClick { onValueChoose() },
        contentAlignment = Center
    ) {
        if (isChoosen) Box(
            modifier = Modifier
                .background(MeverPurple, CircleShape)
                .size(Dp14)
                .padding(Dp3)
        )
        Icon(
            painter = painterResource(R.drawable.ic_circle),
            contentDescription = "Radio button",
            tint = MeverPurple
        )
    }
    Text(
        text = value,
        style = typography.body1,
        color = colorScheme.onPrimary
    )
}