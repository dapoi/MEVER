package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Companion.Round
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.TextUnit
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp1
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp15
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp30
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverButton(
    title: String,
    buttonType: MeverButtonType,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    isLoading: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(Dp30),
    textSize: TextUnit? = null,
    onClick: () -> Unit
) = with(buttonType) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .background(
                color = if (isEnabled) backgroundColor else colorScheme.onSurface,
                shape = shape
            )
            .applyBorder(
                borderColor = borderColor,
                shape = shape
            )
            .clip(shape)
            .onCustomClick(enabled = isEnabled) {
                onClick()
                keyboardController?.hide()
            },
        contentAlignment = Center
    ) {
        when {
            isLoading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(MeverPurple),
                contentAlignment = Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(Dp20),
                    strokeCap = Round,
                    color = contentColor
                )
            }

            title.isNotEmpty() -> Text(
                text = title,
                style = textSize?.let { typography.body2.copy(fontSize = it) } ?: typography.body2,
                color = contentColor,
                modifier = Modifier.padding(horizontal = Dp15, vertical = Dp4)
            )

            else -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentAlignment = Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_download),
                    tint = contentColor,
                    contentDescription = "Download"
                )
            }
        }
    }
}

@Composable
private fun Modifier.applyBorder(
    shape: RoundedCornerShape,
    borderColor: Color? = null,
) = if (borderColor != null) border(width = Dp1, color = borderColor, shape = shape)
else this