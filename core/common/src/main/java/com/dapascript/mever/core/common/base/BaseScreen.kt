package com.dapascript.mever.core.common.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.util.LocalActivity

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun BaseScreen(
    topBarArgs: TopBarArgs = TopBarArgs(),
    useStatusBarsPadding: Boolean = true,
    useNavigationBarsPadding: Boolean = false,
    lockOrientation: Boolean = true,
    content: @Composable () -> Unit
) = with(topBarArgs) {
    val activity = LocalActivity.current

    LaunchedEffect(lockOrientation) {
        if (lockOrientation && SDK_INT < 36) {
            try {
                activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    BaseScreenContent(
        topBarArgs = this@with,
        useStatusBarsPadding = useStatusBarsPadding,
        useNavigationBarsPadding = useNavigationBarsPadding,
        content = content
    )
}

@Composable
private fun BaseScreenContent(
    topBarArgs: TopBarArgs,
    useStatusBarsPadding: Boolean,
    useNavigationBarsPadding: Boolean,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = colorScheme.background)
            .getSystemBarsPadding(useStatusBarsPadding, useNavigationBarsPadding)
    ) {
        content()
        if (topBarArgs.hideDefaultTopBar.not()) MeverTopBar(
            topBarArgs = topBarArgs,
            modifier = Modifier.padding(PaddingValues(horizontal = Dp24))
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