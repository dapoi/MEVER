package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverButtonAttr.MeverButtonType.Outlined
import com.dapascript.mever.core.common.ui.attr.MeverContentViewerAttr.ContentViewerActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverContentViewerAttr.ContentViewerActionMenu.DELETE
import com.dapascript.mever.core.common.ui.attr.MeverContentViewerAttr.ContentViewerActionMenu.SHARE
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.ActionMenu
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp120
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp150
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp64
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.MeverWhite
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.hideSystemBar
import com.dapascript.mever.core.common.util.isSystemBarVisible
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun MeverPhotoViewer(
    source: String,
    preview: String,
    fileName: String,
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    onClickDelete: () -> Unit,
    onClickShare: () -> Unit,
    onClickDownload: (String) -> Unit
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    var isPhotoTouched by remember { mutableStateOf(true) }
    var showDropDownMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isZoomed by remember { mutableStateOf(false) }
    var dragY by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val dismissDistance = with(density) { Dp120.toPx() }
    val bgAlpha = 1f - (abs(dragY) / (dismissDistance * 1.5f)).coerceIn(0f, 0.8f)

    DisposableEffect(lifecycleOwner) {
        onDispose { hideSystemBar(activity, isSystemBarVisible(activity).not()) }
    }

    LaunchedEffect(isPhotoTouched.not()) { hideSystemBar(activity, isPhotoTouched) }

    Box(
        modifier = modifier
            .background(MeverBlack.copy(alpha = bgAlpha))
            .offset { IntOffset(0, dragY.roundToInt()) }
            .pointerInput(isZoomed) {
                detectVerticalDragGestures(
                    onDragCancel = { dragY = 0f },
                    onDragEnd = {
                        if (abs(dragY) > dismissDistance) onClickBack() else dragY = 0f
                    },
                    onVerticalDrag = { change, amount ->
                        if (!isZoomed) {
                            dragY += amount
                            change.consume()
                        }
                    }
                )
            }
    ) {
        PhotoViewer(
            modifier = Modifier
                .wrapContentSize()
                .align(Center),
            source = source,
            preview = preview,
            isPhotoTouched = isPhotoTouched,
            onPhotoTouched = { isPhotoTouched = it },
            onZooming = { isZoomed = it }
        )
        if (preview.isNotEmpty()) MeverButton(
            modifier = Modifier
                .align(BottomCenter)
                .padding(bottom = Dp150),
            title = stringResource(R.string.download),
            buttonType = Outlined(
                contentColor = MeverWhite,
                borderColor = MeverWhite
            )
        ) { onClickDownload(source.ifEmpty { preview }) }
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
                    actionMenus = if (preview.isEmpty()) listOf(
                        ActionMenu(
                            icon = R.drawable.ic_more,
                            nameIcon = "More",
                            onClickActionMenu = { showDropDownMenu = showDropDownMenu.not() }
                        )
                    ) else emptyList(),
                    title = fileName,
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
            listDropDown = ContentViewerActionMenu.entries,
            label = { it.getText(context) },
            showDropDownMenu = showDropDownMenu,
            backgroundColor = MeverDark,
            textColor = MeverWhite,
            onDismissDropDownMenu = { showDropDownMenu = false },
            onClick = { item ->
                when (item) {
                    DELETE -> showDeleteDialog = true
                    SHARE -> onClickShare()
                }
            }
        )
    }

    MeverDialog(
        meverDialogArgs = MeverDialogArgs(
            title = stringResource(R.string.delete_title),
            primaryButtonText = stringResource(R.string.delete_button),
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
            text = stringResource(R.string.delete_desc),
            textAlign = TextAlign.Center,
            style = typography.body1,
            color = MeverWhite
        )
    }
}

@Composable
private fun PhotoViewer(
    source: String,
    preview: String,
    isPhotoTouched: Boolean,
    modifier: Modifier = Modifier,
    onPhotoTouched: (Boolean) -> Unit,
    onZooming: (Boolean) -> Unit
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
                                onZooming(false)
                                onPhotoTouched(false)
                            } else {
                                scale = 2f
                                onZooming(true)
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
                                onZooming(newScale > 1f)
                                onPhotoTouched(true)
                                event.changes.forEach { it.consume() }
                            } else if (zoom != 1f) {
                                val newScale = (scale * zoom).coerceIn(minScale, maxScale)
                                scale = newScale
                                onZooming(newScale > 1f)
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
        var isImageError by remember { mutableStateOf(false) }
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = if (isImageError && preview.isNotEmpty()) preview else source,
            contentDescription = "Photo Viewer",
            loading = {
                if (preview.isNotEmpty()) Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(preview),
                    contentDescription = "Loading Image"
                ) else null
            },
            error = { isImageError = true }
        )
    }
}