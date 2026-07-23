package com.dapascript.mever.core.common.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.ui.theme.MeverTheme.colors
import com.dapascript.mever.core.common.util.LocalActivity

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun BaseScreen(
    topBarArgs: TopBarArgs = TopBarArgs(),
    hideDefaultTopBar: Boolean = false,
    useStatusBarsPadding: Boolean = true,
    useNavigationBarsPadding: Boolean = false,
    lockOrientation: Boolean = true,
    backgroundColor: Color? = null,
    onBackHandler: () -> Unit,
    content: @Composable () -> Unit
) {
    val activity = LocalActivity.current
    val currentOnBack by rememberUpdatedState(onBackHandler)

    LaunchedEffect(lockOrientation) {
        if (lockOrientation) {
            try {
                activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    BackHandler { currentOnBack() }

    BaseScreenContent(
        topBarArgs = topBarArgs,
        hideDefaultTopBar = hideDefaultTopBar,
        useStatusBarsPadding = useStatusBarsPadding,
        useNavigationBarsPadding = useNavigationBarsPadding,
        backgroundColor = backgroundColor,
        onBackHandler = { currentOnBack() }
    ) { content() }
}

@Composable
private fun BaseScreenContent(
    topBarArgs: TopBarArgs,
    hideDefaultTopBar: Boolean,
    useStatusBarsPadding: Boolean,
    useNavigationBarsPadding: Boolean,
    backgroundColor: Color?,
    onBackHandler: () -> Unit,
    content: @Composable () -> Unit
) {
    val systemBarsPadding = remember(useStatusBarsPadding, useNavigationBarsPadding) {
        Modifier.getSystemBarsPadding(useStatusBarsPadding, useNavigationBarsPadding)
    }

    Box(
        modifier = Modifier
            .background(color = backgroundColor ?: colors.whiteDark)
            .then(systemBarsPadding)
    ) {
        content()
        if (hideDefaultTopBar.not()) MeverTopBar(
            topBarArgs = topBarArgs,
            modifier = Modifier.padding(PaddingValues(horizontal = Dp24)),
            onClickBack = onBackHandler
        )
    }
}

private fun Modifier.getSystemBarsPadding(
    useStatusBarsPadding: Boolean,
    useNavigationBarsPadding: Boolean
) = when {
    useStatusBarsPadding && useNavigationBarsPadding -> this.systemBarsPadding()
    useStatusBarsPadding -> this.statusBarsPadding()
    useNavigationBarsPadding -> this.navigationBarsPadding()
    else -> this
}