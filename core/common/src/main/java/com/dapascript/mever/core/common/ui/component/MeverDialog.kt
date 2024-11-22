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
import androidx.compose.foundation.layout.IntrinsicSize.Min
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.window.Dialog
import com.dapascript.mever.core.common.ui.attr.MeverDownloadAttr.MeverDownloadArgs
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp0
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp14
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp2
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp20
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp4
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverBlack
import com.dapascript.mever.core.common.ui.theme.MeverPurple
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.clickableSingle
import com.dapascript.mever.core.common.util.isValidUrl

@Composable
fun MeverDialog(
    showDialog: Boolean,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    var showAnimatedDialog by remember { mutableStateOf(false) }

    LaunchedEffect(showDialog) { if (showDialog) showAnimatedDialog = true }

    if (showAnimatedDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = modifier.fillMaxSize(),
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
                            .clip(RoundedCornerShape(Dp16))
                            .background(colorScheme.surface),
                        contentAlignment = Center
                    ) { content() }

                    DisposableEffect(Unit) { onDispose { showAnimatedDialog = false } }
                }
            }
        }
    }
}

@Composable
fun PermissionDialog(
    isPermissionsDeclined: Boolean,
    descriptionPermission: String,
    modifier: Modifier = Modifier,
    onGoToSetting: () -> Unit,
    onAllow: () -> Unit,
    onDismiss: () -> Unit
) = Column(
    modifier = modifier
        .background(colorScheme.surface)
        .padding(horizontal = Dp16, vertical = Dp8),
    verticalArrangement = spacedBy(Dp8),
) {
    Text(
        text = "Permission required",
        style = typography.bodyBold1
    )
    Text(
        text = descriptionPermission,
        style = typography.body1
    )
    Row(
        modifier = Modifier.height(Min),
        verticalAlignment = CenterVertically,
        horizontalArrangement = SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(Dp14))
                .clickableSingle { onDismiss() }
                .weight(1f)
                .padding(vertical = Dp16),
            contentAlignment = Center
        ) {
            Text(
                text = "Cancel",
                style = typography.bodyBold2
            )
        }
        Box(
            modifier = Modifier
                .width(Dp2)
                .height(Dp20)
                .background(
                    color = colorScheme.onSurface.copy(alpha = .08f),
                    shape = RoundedCornerShape(Dp8)
                )
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(Dp14))
                .clickableSingle { if (isPermissionsDeclined) onGoToSetting() else onAllow() }
                .weight(1f)
                .padding(vertical = Dp16),
            contentAlignment = Center
        ) {
            Text(
                text = if (isPermissionsDeclined) "Go to setting" else "Allow",
                style = typography.bodyBold2,
                color = MeverPurple
            )
        }
    }
}

@Composable
fun DownloadDialog(
    meverDownloadArgs: List<MeverDownloadArgs>,
    modifier: Modifier = Modifier,
    onDownloadClick: (String) -> Unit,
    onDismiss: () -> Unit
) = with(meverDownloadArgs) {
    var chooseQuality by remember {
        mutableStateOf(if (any { it.quality.isNotEmpty() }) get(0).url else "")
    }

    Column(
        modifier = modifier
            .background(colorScheme.surface)
            .padding(vertical = Dp8, horizontal = Dp16),
        verticalArrangement = spacedBy(Dp8),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Your video is ready to download",
            style = typography.bodyBold1,
            color = MeverBlack,
            modifier = Modifier.align(CenterHorizontally)
        )
        HorizontalDivider(color = colorScheme.onSurface.copy(alpha = .08f))
        MeverThumbnail(
            url = size.takeIf { it > 0 }?.let { get(0).url } ?: "",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(Dp8))
        filter { it.quality.isNotEmpty() && it.url.isValidUrl() }.forEach { (url, quality) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(Dp4))
                    .clickableSingle { chooseQuality = url },
                horizontalArrangement = SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Text(
                    text = quality,
                    style = typography.body1
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp0) {
                    RadioButton(
                        modifier = Modifier.padding(vertical = Dp8),
                        colors = colors(selectedColor = MeverPurple),
                        selected = chooseQuality == url,
                        onClick = { chooseQuality = url }
                    )
                }
            }
        }
        Row(
            modifier = Modifier.height(Min),
            verticalAlignment = CenterVertically,
            horizontalArrangement = SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dp14))
                    .clickableSingle { onDismiss() }
                    .weight(1f)
                    .padding(vertical = Dp14),
                contentAlignment = Center
            ) {
                Text(
                    text = "Cancel",
                    style = typography.bodyBold2
                )
            }
            Box(
                modifier = Modifier
                    .width(Dp2)
                    .height(Dp20)
                    .background(
                        color = colorScheme.onSurface.copy(alpha = .08f),
                        shape = RoundedCornerShape(Dp8)
                    )
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(Dp14))
                    .clickableSingle { onDownloadClick(chooseQuality.ifEmpty { get(0).url }) }
                    .weight(1f)
                    .padding(vertical = Dp14),
                contentAlignment = Center
            ) {
                Text(
                    text = "Download",
                    style = typography.bodyBold2,
                    color = MeverPurple
                )
            }
        }
    }
}