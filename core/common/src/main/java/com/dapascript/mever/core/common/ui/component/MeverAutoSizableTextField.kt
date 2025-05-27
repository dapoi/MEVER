package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import androidx.compose.ui.text.input.KeyboardType.Companion.Uri
import androidx.compose.ui.text.style.TextAlign.Companion.End
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp22
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp3
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.TextDimens.Sp32
import com.dapascript.mever.core.common.util.clearFocusOnKeyboardDismiss
import com.dapascript.mever.core.common.util.onCustomClick
import kotlin.Int.Companion.MAX_VALUE
import kotlin.math.ceil

@Composable
fun MeverAutoSizableTextField(
    value: String,
    minFontSize: TextUnit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = Sp32,
    maxLines: Int = MAX_VALUE,
    shape: RoundedCornerShape = RoundedCornerShape(Dp12),
    scaleFactor: Float = 0.9f,
    onValueChange: (String) -> Unit
) = BoxWithConstraints(modifier = modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current
    var nFontSize = fontSize
    val calculateParagraph = @Composable {
        Paragraph(
            text = value,
            style = typography.body1.copy(fontSize = nFontSize),
            constraints = Constraints(
                maxWidth = ceil(with(LocalDensity.current) { maxWidth.toPx() }).toInt()
            ),
            density = LocalDensity.current,
            fontFamilyResolver = LocalFontFamilyResolver.current,
            maxLines = maxLines
        )
    }

    var intrinsics = calculateParagraph()
    with(LocalDensity.current) {
        while ((intrinsics.height.toDp() > maxHeight || intrinsics.didExceedMaxLines) && nFontSize >= minFontSize) {
            nFontSize *= scaleFactor
            intrinsics = calculateParagraph()
        }
    }

    Column(
        modifier = Modifier
            .shadow(elevation = Dp3, shape = shape)
            .background(color = colorScheme.surface, shape = shape)
            .clip(shape),
        verticalArrangement = SpaceBetween
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp8)
                .weight(1f)
                .clearFocusOnKeyboardDismiss(focusManager),
            textStyle = typography.body1.copy(
                fontSize = nFontSize,
                color = colorScheme.onPrimary
            ),
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(keyboardType = Uri, imeAction = Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            value = value,
            maxLines = maxLines,
            onValueChange = onValueChange,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) Text(
                    text = stringResource(R.string.type_anything),
                    style = typography.body1.copy(color = colorScheme.secondary)
                )
                innerTextField()
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp8),
            horizontalArrangement = SpaceBetween
        ) {
            if (value.isNotEmpty()) Icon(
                modifier = Modifier
                    .size(Dp22)
                    .clip(shape)
                    .onCustomClick { onValueChange("") },
                imageVector = ImageVector.vectorResource(R.drawable.ic_replay),
                tint = colorScheme.onPrimary,
                contentDescription = "Clear"
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "${value.length} / 300",
                textAlign = End
            )
        }
    }
}