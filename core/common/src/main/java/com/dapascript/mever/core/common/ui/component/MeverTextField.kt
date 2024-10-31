package com.dapascript.mever.core.common.ui.component

import android.R.drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.MeverGray
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.util.clearFocusOnKeyboardDismiss
import com.dapascript.mever.core.common.util.isValidUrl

@Composable
fun MeverTextField(
    webDomainValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit
) = Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = spacedBy(Dp16)
) {
    MeverTextFieldComponent(webDomainValue) { onValueChange(it) }
    MeverDownloadButton(enabled = webDomainValue.text.isValidUrl())
}

@Composable
private fun RowScope.MeverTextFieldComponent(
    webDomainValue: TextFieldValue,
    cornerShape: RoundedCornerShape = RoundedCornerShape(Dp48),
    onValueChange: (TextFieldValue) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .weight(1f)
            .shadow(elevation = Dp2, shape = cornerShape)
            .background(color = colorScheme.surface, shape = cornerShape),
        verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Dp16, vertical = Dp14)
                .size(Dp24)
                .background(color = Transparent, shape = CircleShape)
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.ic_link),
                contentDescription = "Link",
                tint = colorScheme.onPrimary
            )
        }
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .clearFocusOnKeyboardDismiss(focusManager),
            value = webDomainValue.text,
            onValueChange = { onValueChange(TextFieldValue(it)) },
            interactionSource = interactionSource,
            maxLines = 1,
            singleLine = true,
            cursorBrush = SolidColor(MeverPurple),
            textStyle = typography.bodyMedium,
            keyboardOptions = KeyboardOptions(keyboardType = Uri, imeAction = Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            decorationBox = { innerTextField ->
                if (webDomainValue.text.isEmpty()) Text(
                    text = "Paste link here...",
                    color = MeverGray,
                    style = MeverTheme.typography.body2
                )
                innerTextField()
            }
        )
        if (webDomainValue.text.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(horizontal = Dp16, vertical = Dp14)
                    .size(Dp24)
                    .clickable { onValueChange(TextFieldValue()) }
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = drawable.ic_menu_close_clear_cancel),
                    contentDescription = "Clear",
                    tint = colorScheme.onPrimary
                )
            }
        }
    }
}