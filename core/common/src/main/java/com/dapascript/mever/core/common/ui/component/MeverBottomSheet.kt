package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp96

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeverBottomSheet(
    showBottomSheet: Boolean,
    modifier: Modifier = Modifier,
    onClickDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            containerColor = colorScheme.background,
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false),
            dragHandle = {
                HorizontalDivider(
                    thickness = Dp4,
                    color = colorScheme.onPrimary,
                    modifier = Modifier
                        .width(Dp96)
                        .padding(vertical = Dp24)
                )
            },
            onDismissRequest = { onClickDismiss() }
        ) { content() }
    }
}