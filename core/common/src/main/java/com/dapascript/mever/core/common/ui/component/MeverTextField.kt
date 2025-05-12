package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp48
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.clearFocusOnKeyboardDismiss
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverTextField(
    webDomainValue: TextFieldValue,
    shape: RoundedCornerShape = RoundedCornerShape(Dp48),
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .padding(vertical = Dp3)
            .shadow(elevation = Dp3, shape = shape)
            .background(color = colorScheme.surface, shape = shape)
            .clip(shape),
        verticalAlignment = CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = Dp16, vertical = Dp12)
                .clip(CircleShape)
                .background(color = Transparent, shape = CircleShape)
                .size(Dp24)
                .onCustomClick { clipboardManager.getText()?.text?.let { onValueChange(TextFieldValue(it)) } }
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.ic_link),
                contentDescription = "Link",
                tint = colorScheme.onPrimary
            )
        }
        BasicTextField(
            modifier = Modifier
                .weight(1f)
                .clearFocusOnKeyboardDismiss(focusManager),
            value = webDomainValue.text.trim(),
            onValueChange = { onValueChange(TextFieldValue(it)) },
            interactionSource = interactionSource,
            maxLines = 1,
            singleLine = true,
            cursorBrush = SolidColor(colorScheme.onPrimary),
            textStyle = typography.body2.copy(color = colorScheme.onPrimary),
            keyboardOptions = KeyboardOptions(keyboardType = Uri, imeAction = Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            decorationBox = { innerTextField ->
                if (webDomainValue.text.isEmpty()) Text(
                    text = stringResource(R.string.paste_url),
                    style = typography.body2.copy(color = colorScheme.secondary)
                )
                innerTextField()
            }
        )
        if (webDomainValue.text.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(horizontal = Dp16, vertical = Dp12)
                    .clip(CircleShape)
                    .size(Dp24)
                    .clickable { onValueChange(TextFieldValue()) }
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.ic_clear),
                    contentDescription = "Clear",
                    tint = colorScheme.onPrimary
                )
            }
        }
    }
}