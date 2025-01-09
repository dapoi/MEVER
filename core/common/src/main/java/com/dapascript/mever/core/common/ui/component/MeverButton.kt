package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp30
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp44
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.clickableSingle

@Composable
fun MeverButton(
    enabled: Boolean,
    isLoading: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(Dp30),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .background(color = if (enabled) MeverPurple else colorScheme.onSurface, shape = shape)
            .fillMaxWidth()
            .height(Dp44)
            .clip(shape)
            .clickableSingle(enabled = enabled) {
                onClick()
                keyboardController?.hide()
            },
        contentAlignment = Center
    ) {
        if (isLoading) Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MeverPurple),
            contentAlignment = Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dp20),
                strokeCap = Round,
                color = colorScheme.onSecondary
            )
        } else Text(
            text = "Download",
            style = typography.body2,
            color = colorScheme.onSecondary
        )
    }
}