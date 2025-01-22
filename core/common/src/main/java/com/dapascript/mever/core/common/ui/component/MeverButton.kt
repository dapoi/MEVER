package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.FILLED
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.OUTLINED
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp30
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.clickableSingle

@Composable
fun MeverButton(
    title: String,
    buttonType: MeverButtonType,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(Dp30),
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .background(color = getButtonColor(isEnabled, buttonType), shape = shape)
            .setBorder(isTypeOutlined = buttonType == OUTLINED, shape = shape)
            .clip(shape)
            .clickableSingle(enabled = isEnabled) {
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
            text = title,
            style = typography.body2,
            color = if (buttonType == FILLED) colorScheme.onSecondary else MeverPurple,
            modifier = Modifier.padding(horizontal = Dp15, vertical = Dp4)
        )
    }
}

@Composable
private fun getButtonColor(
    isEnabled: Boolean,
    buttonType: MeverButtonType
) = when (buttonType) {
    FILLED -> if (isEnabled) MeverPurple else colorScheme.onSurface
    OUTLINED -> colorScheme.background
}

@Composable
private fun Modifier.setBorder(
    isTypeOutlined: Boolean,
    shape: RoundedCornerShape
) = if (isTypeOutlined) border(width = Dp1, color = MeverPurple, shape = shape)
else this