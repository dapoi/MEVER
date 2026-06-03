package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale.Companion.FillBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp200
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Suppress("ASSIGNED_VALUE_IS_NEVER_READ")
@Composable
fun MeverDialog(
    showDialog: Boolean,
    image: Int? = R.drawable.ic_error,
    title: String? = stringResource(R.string.error_title),
    description: String? = stringResource(R.string.error_desc),
    primaryActionLabel: String? = stringResource(R.string.retry),
    secondaryActionLabel: String? = stringResource(R.string.cancel),
    titleColor: Color? = null,
    descriptionColor: Color? = null,
    backgroundColor: Color? = null,
    primaryActionColor: Color? = null,
    secondaryActionColor: Color? = null,
    onClickPrimaryAction: (() -> Unit)? = null,
    onClickSecondaryAction: (() -> Unit)? = null
) {
    val currentPrimary = rememberUpdatedState(onClickPrimaryAction)
    val currentSecondary = rememberUpdatedState(onClickSecondaryAction)
    var showAnimatedDialog by remember { mutableStateOf(false) }
    val onDismiss = {
        if (currentSecondary.value != null) {
            currentSecondary.value?.invoke()
        } else {
            currentPrimary.value?.invoke()
        }
        showAnimatedDialog = false
    }

    LaunchedEffect(showDialog) { if (showDialog) showAnimatedDialog = true }

    if (showAnimatedDialog) {
        Dialog(
            properties = DialogProperties(),
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss() }
                    },
                contentAlignment = Center
            ) {
                var animateIn by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) { animateIn = true }

                AnimatedVisibility(
                    visible = animateIn && showDialog,
                    enter = fadeIn(spring(stiffness = StiffnessHigh)) + scaleIn(
                        initialScale = .8f,
                        animationSpec = spring(
                            dampingRatio = DampingRatioMediumBouncy,
                            stiffness = StiffnessMediumLow
                        )
                    ),
                    exit = scaleOut(
                        targetScale = 0.8f,
                        animationSpec = tween(durationMillis = 200)
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = 200)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) { detectTapGestures {} }
                            .shadow(elevation = Dp8, shape = RoundedCornerShape(Dp16))
                            .clip(RoundedCornerShape(Dp16)),
                        contentAlignment = Center
                    ) {
                        DialogContent(
                            title = title,
                            image = image,
                            description = description,
                            primaryActionLabel = primaryActionLabel,
                            secondaryActionLabel = secondaryActionLabel,
                            titleColor = titleColor,
                            descriptionColor = descriptionColor,
                            backgroundColor = backgroundColor,
                            primaryActionColor = primaryActionColor,
                            secondaryActionColor = secondaryActionColor,
                            onClickPrimaryAction = {
                                currentPrimary.value?.invoke()
                                showAnimatedDialog = false
                            },
                            onClickSecondaryAction = {
                                currentSecondary.value?.invoke()
                                showAnimatedDialog = false
                            }
                        )
                    }

                    DisposableEffect(Unit) {
                        onDispose { showAnimatedDialog = false }
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogContent(
    title: String?,
    image: Int?,
    description: String?,
    primaryActionLabel: String?,
    secondaryActionLabel: String?,
    titleColor: Color?,
    descriptionColor: Color?,
    backgroundColor: Color?,
    primaryActionColor: Color?,
    secondaryActionColor: Color?,
    onClickPrimaryAction: (() -> Unit)?,
    onClickSecondaryAction: (() -> Unit)?
) = Column(
    modifier = Modifier
        .background(backgroundColor ?: colors.whiteDark)
        .padding(top = Dp12, start = Dp16, end = Dp16, bottom = Dp8),
    verticalArrangement = spacedBy(Dp12),
    horizontalAlignment = CenterHorizontally
) {
    title?.let {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = typography.bodyBold1,
            color = titleColor ?: colors.blackWhite
        )
    }
    image?.let {
        MeverImage(
            modifier = Modifier
                .size(Dp200)
                .align(CenterHorizontally),
            source = image,
            contentScale = FillBounds
        )
    }
    description?.let {
        Text(
            text = description,
            textAlign = TextAlign.Center,
            style = typography.body1,
            color = descriptionColor ?: colors.blackWhite
        )
    }
    Row(
        modifier = Modifier.height(Min),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween
    ) {
        secondaryActionLabel?.let {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dp14))
                    .onCustomClick { onClickSecondaryAction?.invoke() }
                    .weight(1f)
                    .padding(vertical = Dp8),
                contentAlignment = Center
            ) {
                Text(
                    text = secondaryActionLabel,
                    style = typography.bodyBold2,
                    color = secondaryActionColor ?: colors.blackWhite
                )
            }
        }
        if (primaryActionLabel != null && secondaryActionLabel != null) {
            Box(
                modifier = Modifier
                    .width(Dp2)
                    .height(Dp20)
                    .background(
                        color = colors.blackWhite.copy(alpha = .08f),
                        shape = RoundedCornerShape(Dp8)
                    )
            )
        }
        primaryActionLabel?.let {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dp14))
                    .onCustomClick { onClickPrimaryAction?.invoke() }
                    .weight(1f)
                    .padding(vertical = Dp8),
                contentAlignment = Center
            ) {
                Text(
                    text = primaryActionLabel,
                    style = typography.bodyBold2,
                    color = primaryActionColor ?: colors.alwaysPurple
                )
            }
        }
    }
}