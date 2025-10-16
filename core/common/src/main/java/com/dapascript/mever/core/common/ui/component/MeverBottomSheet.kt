package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults.ExpandedShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.SheetValue.PartiallyExpanded
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeverBottomSheet(
    showBottomSheet: Boolean,
    modifier: Modifier = Modifier,
    skipPartiallyExpanded: Boolean = true,
    shouldDismissOnClickOutside: Boolean = false,
    onDismissBottomSheet: () -> Unit,
    content: @Composable () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded)
    var shouldShowBottomSheet by remember { mutableStateOf(false) }
    val targetCorner = when (sheetState.targetValue) {
        Expanded -> Dp0
        PartiallyExpanded, Hidden -> Dp24
    }
    val corner by animateDpAsState(
        targetValue = targetCorner,
        animationSpec = spring(
            stiffness = StiffnessMediumLow,
            dampingRatio = DampingRatioNoBouncy
        ),
        label = "sheetCorner"
    )

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
        shape = if (skipPartiallyExpanded.not()) RoundedCornerShape(
            topStart = corner,
            topEnd = corner
        ) else ExpandedShape,
        containerColor = colorScheme.background,
        properties = ModalBottomSheetProperties(shouldDismissOnClickOutside = shouldDismissOnClickOutside),
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