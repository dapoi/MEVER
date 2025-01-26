package com.dapascript.mever.core.common.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessMediumLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.clickableSingle

@Composable
fun MeverDialog(
    showDialog: Boolean,
    meverDialogArgs: MeverDialogArgs,
    hideInteractionButton: Boolean = false,
    modifier: Modifier = Modifier,
    contentBody: @Composable ColumnScope.() -> Unit
) = with(meverDialogArgs) {
    var showAnimatedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showDialog) { if (showDialog) showAnimatedDialog = true }

    if (showAnimatedDialog) {
        Dialog(
            properties = DialogProperties(),
            onDismissRequest = onClickSecondaryButton
        ) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures { if (hideInteractionButton) onClickSecondaryButton() } },
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
                    exit = slideOutVertically { it / 8 } + fadeOut() + scaleOut(targetScale = .95f)
                ) {
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) { detectTapGestures {} }
                            .shadow(elevation = Dp8, shape = RoundedCornerShape(Dp16))
                            .clip(RoundedCornerShape(Dp16)),
                        contentAlignment = Center
                    ) {
                        DialogContent(
                            meverDialogArgs = this@with,
                            hideInteractionButton = hideInteractionButton,
                            contentBody = contentBody
                        )
                    }

                    DisposableEffect(LocalView.current.parent) { onDispose { showAnimatedDialog = false } }
                }
            }
        }
    }
}

@Composable
private fun DialogContent(
    meverDialogArgs: MeverDialogArgs,
    hideInteractionButton: Boolean,
    modifier: Modifier = Modifier,
    contentBody: @Composable ColumnScope.() -> Unit
) = with(meverDialogArgs) {
    Column(
        modifier = modifier
            .background(backgroundColor ?: colorScheme.background)
            .padding(vertical = Dp8, horizontal = Dp16),
        verticalArrangement = spacedBy(Dp12),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = title,
            style = typography.bodyBold1,
            color = titleColor ?: colorScheme.onPrimary
        )
        contentBody()
        if (hideInteractionButton.not()) Row(
            modifier = Modifier.height(Min),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dp14))
                    .clickableSingle { onClickSecondaryButton() }
                    .weight(1f)
                    .padding(vertical = Dp16),
                contentAlignment = Center
            ) {
                Text(
                    text = secondaryButtonText ?: stringResource(R.string.cancel),
                    style = typography.bodyBold2,
                    color = secondaryButtonColor ?: colorScheme.onPrimary
                )
            }
            Box(
                modifier = Modifier
                    .width(Dp2)
                    .height(Dp20)
                    .background(
                        color = colorScheme.onPrimary.copy(alpha = .08f),
                        shape = RoundedCornerShape(Dp8)
                    )
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dp14))
                    .clickableSingle { onClickPrimaryButton() }
                    .weight(1f)
                    .padding(vertical = Dp16),
                contentAlignment = Center
            ) {
                Text(
                    text = primaryButtonText,
                    style = typography.bodyBold2,
                    color = primaryButtonColor ?: colorScheme.primary
                )
            }
        }
    }
}