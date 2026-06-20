package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.core.Spring.DampingRatioNoBouncy
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults.ExpandedShape
import androidx.compose.material3.BottomSheetDefaults.HiddenShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp80
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeverBottomSheet(
    showBottomSheet: Boolean,
    modifier: Modifier = Modifier,
    isAlwaysRectangular: Boolean = false,
    isDisableContentDrag: Boolean = false,
    skipPartiallyExpanded: Boolean = true,
    shouldDismissOnBackPress: Boolean = true,
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
    val nestedScrollConnection = remember(isDisableContentDrag) {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset = if (isDisableContentDrag) available else Offset.Zero

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity
            ): Velocity = if (isDisableContentDrag) available else Velocity.Zero
        }
    }

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
        shape = when {
            skipPartiallyExpanded.not() -> RoundedCornerShape(
                topStart = corner,
                topEnd = corner
            )

            isAlwaysRectangular -> HiddenShape
            else -> ExpandedShape
        },
        containerColor = colors.whiteDark,
        properties = ModalBottomSheetProperties(
            shouldDismissOnClickOutside = shouldDismissOnClickOutside,
            shouldDismissOnBackPress = shouldDismissOnBackPress
        ),
        dragHandle = {
            HorizontalDivider(
                thickness = Dp2,
                color = colors.blackWhite,
                modifier = Modifier
                    .width(Dp80)
                    .padding(vertical = Dp24)
            )
        },
        onDismissRequest = onDismissBottomSheet
    ) {
        val view = LocalView.current
        val isAppInDarkMode = colors.whiteDark != MeverWhite

        LaunchedEffect(view, isAppInDarkMode) {
            val window = (view.parent as? DialogWindowProvider)?.window
            if (window != null) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = isAppInDarkMode.not()
                    isAppearanceLightNavigationBars = isAppInDarkMode.not()
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(nestedScrollConnection)
        ) { content() }
    }
}