package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale.Companion.Fit
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.convertFilename
import com.dapascript.mever.core.common.util.hideSystemBar
import com.dapascript.mever.core.common.util.isSystemBarVisible

@Composable
fun MeverPhotoViewer(
    source: String,
    modifier: Modifier = Modifier,
    onClickDelete: () -> Unit,
    onClickShare: () -> Unit,
    onClickBack: () -> Unit
) {
    val activity = LocalActivity.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    var isPhotoTouched by remember { mutableStateOf(false) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        onDispose { hideSystemBar(activity, isSystemBarVisible(activity).not()) }
    }

    LaunchedEffect(isPhotoTouched.not()) { hideSystemBar(activity, isPhotoTouched) }

    Box(modifier = modifier.background(MeverBlack)) {
        PhotoViewer(
            modifier = Modifier
                .wrapContentSize()
                .align(Center),
            image = source,
            isPhotoTouched = isPhotoTouched
        ) { isPhotoTouched = it }
        AnimatedVisibility(
            visible = isPhotoTouched.not(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            MeverTopBar(
                modifier = Modifier
                    .padding(horizontal = Dp24)
                    .systemBarsPadding(),
                topBarArgs = TopBarArgs(
                    actionMenus = listOf(
                        ActionMenu(
                            icon = R.drawable.ic_more,
                            nameIcon = "More",
                            onClickActionMenu = { showDropDownMenu = showDropDownMenu.not() }
                        )
                    ),
                    title = convertFilename(source.substringAfterLast("/")),
                    topBarColor = MeverTransparent,
                    titleColor = MeverWhite,
                    iconBackColor = MeverWhite,
                    actionMenusColor = MeverWhite,
                    onClickBack = onClickBack
                ),
                useCenterTopBar = false
            )
        }
        MeverPopupDropDownMenu(
            modifier = Modifier
                .padding(PaddingValues(top = Dp64, end = Dp24))
                .statusBarsPadding(),
            listDropDown = listOf("Delete", "Share"),
            showDropDownMenu = showDropDownMenu,
            backgroundColor = MeverDark,
            textColor = MeverWhite,
            onDismissDropDownMenu = { showDropDownMenu = false },
            onClick = { item ->
                when (item) {
                    "Delete" -> showDeleteDialog = true
                    "Share" -> onClickShare()
                }
            }
        )
    }

    MeverDialog(
        meverDialogArgs = MeverDialogArgs(
            title = "Delete this file?",
            primaryButtonText = "Delete",
            titleColor = MeverWhite,
            backgroundColor = MeverDark,
            secondaryButtonColor = MeverWhite,
            onClickPrimaryButton = {
                onClickDelete()
                onClickBack()
                showDeleteDialog = false
            },
            onClickSecondaryButton = {
                hideSystemBar(activity, true)
                showDeleteDialog = false
            }
        ),
        showDialog = showDeleteDialog
    ) {
        Text(
            text = "File that has been deleted cannot be recovered",
            style = typography.body1,
            color = MeverWhite
        )
    }
}

@Composable
private fun PhotoViewer(
    image: String,
    isPhotoTouched: Boolean,
    modifier: Modifier = Modifier,
    onPhotoTouched: (Boolean) -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val minScale = 1f
    val maxScale = 4f

    Box(
        modifier = modifier
            .clipToBounds()
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown()

                    val longPressTimeout = viewConfiguration.longPressTimeoutMillis
                    val doubleTapTimeout = viewConfiguration.doubleTapTimeoutMillis

                    val firstTapUp = withTimeoutOrNull(longPressTimeout) {
                        waitForUpOrCancellation()
                    }

                    if (firstTapUp != null) {
                        val secondTapDown = withTimeoutOrNull(doubleTapTimeout) {
                            awaitFirstDown()
                        }

                        if (secondTapDown != null) {
                            if (scale != 1f) {
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                                onPhotoTouched(false)
                            } else {
                                scale = 2f
                                onPhotoTouched(true)
                            }
                            secondTapDown.consume()
                        } else {
                            onPhotoTouched(isPhotoTouched.not())
                        }
                    } else {
                        do {
                            val event = awaitPointerEvent()
                            val zoom = event.calculateZoom()
                            val pan = event.calculatePan()

                            if (scale > 1f) {
                                val newScale = (scale * zoom).coerceIn(minScale, maxScale)
                                val maxOffsetX = (size.width / 2f) * (newScale - 1)
                                val minOffsetX = -maxOffsetX
                                val maxOffsetY = (size.height / 2f) * (newScale - 1)
                                val minOffsetY = -maxOffsetY

                                offsetX = (offsetX + pan.x).coerceIn(minOffsetX, maxOffsetX)
                                offsetY = (offsetY + pan.y).coerceIn(minOffsetY, maxOffsetY)
                                scale = newScale

                                onPhotoTouched(true)

                                event.changes.forEach { it.consume() }
                            } else if (zoom != 1f) {
                                val newScale = (scale * zoom).coerceIn(minScale, maxScale)
                                scale = newScale

                                onPhotoTouched(true)

                                event.changes.forEach { it.consume() }
                            }
                        } while (event.changes.any { it.pressed })
                    }
                }
            }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                translationX = offsetX
                translationY = offsetY
            }
    ) {
        MeverImage(
            source = image,
            contentScale = Fit
        )
    }
}