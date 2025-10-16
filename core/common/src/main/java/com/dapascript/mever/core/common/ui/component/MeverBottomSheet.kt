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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeverBottomSheet(
    showBottomSheet: Boolean,
    modifier: Modifier = Modifier,
    onDismissBottomSheet: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(true)
    var shouldShowBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(showBottomSheet) {
        if (showBottomSheet) {
            shouldShowBottomSheet = true
        } else {
            sheetState.hide()
            shouldShowBottomSheet = false
        }
    }

    if (shouldShowBottomSheet) ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        containerColor = colorScheme.background,
        properties = ModalBottomSheetProperties(shouldDismissOnClickOutside = false),
        dragHandle = {
            HorizontalDivider(
                thickness = Dp2,
                color = colorScheme.onPrimary,
                modifier = Modifier
                    .width(Dp80)
                    .padding(vertical = Dp24)
            )
        },
        onDismissRequest = onDismissBottomSheet
    ) { content() }
}