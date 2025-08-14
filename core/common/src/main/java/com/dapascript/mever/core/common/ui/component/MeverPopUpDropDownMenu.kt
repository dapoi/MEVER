package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.PopupProperties
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography

@Composable
fun <T> MeverPopupDropDownMenu(
    listDropDown: List<T>,
    showDropDownMenu: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    textColor: Color? = null,
    shape: Shape? = null,
    label: (T) -> String,
    onClick: (T) -> Unit,
    onDismissDropDownMenu: (Boolean) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentSize(TopEnd)
    ) {
        DropdownMenu(
            expanded = showDropDownMenu,
            properties = PopupProperties(focusable = false),
            containerColor = backgroundColor ?: colorScheme.onPrimaryContainer,
            shape = shape ?: RoundedCornerShape(Dp8),
            onDismissRequest = { onDismissDropDownMenu(false) }
        ) {
            listDropDown.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label(item),
                            style = typography.body1,
                            color = textColor ?: colorScheme.onPrimary
                        )
                    },
                    onClick = {
                        onClick(item)
                        onDismissDropDownMenu(false)
                    }
                )
            }
        }
    }
}