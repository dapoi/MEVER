package com.dapascript.mever.core.common.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.ui.attr.MeverTopBarAttr.TopBarArgs
import com.dapascript.mever.core.common.ui.component.MeverTopBar
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp24
import com.dapascript.mever.core.common.util.LocalActivity

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    topBarArgs: TopBarArgs = TopBarArgs(),
    useCenterTopBar: Boolean = true,
    useSystemBarsPadding: Boolean = true,
    allowScreenOverlap: Boolean = false,
    hideDefaultTopBar: Boolean = false,
    lockOrientation: Boolean = true,
    statusBarColor: Color? = null,
    navigationBarColor: Color? = null,
    content: @Composable () -> Unit
) = with(topBarArgs) {
    val activity = LocalActivity.current
    if (lockOrientation) activity.requestedOrientation = SCREEN_ORIENTATION_PORTRAIT

    Box {
        statusBarColor?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(it)
                    .windowInsetsTopHeight(WindowInsets.statusBars)
            )
        }
        navigationBarColor?.let {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(it)
                    .align(BottomCenter)
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
            )
        }
        BaseScreenContent(
            topBarArgs = this@with,
            useCenterTopBar = useCenterTopBar,
            useSystemBarsPadding = useSystemBarsPadding,
            allowScreenOverlap = allowScreenOverlap,
            hideDefaultTopBar = hideDefaultTopBar,
            modifier = modifier,
            content = content
        )
    }
}

@Composable
private fun BaseScreenContent(
    topBarArgs: TopBarArgs,
    useCenterTopBar: Boolean,
    useSystemBarsPadding: Boolean,
    allowScreenOverlap: Boolean,
    hideDefaultTopBar: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (allowScreenOverlap) {
        Box(
            modifier = modifier
                .background(color = colorScheme.background)
                .then(if (useSystemBarsPadding) Modifier.systemBarsPadding() else Modifier)
        ) {
            content()
            if (hideDefaultTopBar.not()) MeverTopBar(
                topBarArgs = topBarArgs,
                useCenterTopBar = useCenterTopBar,
                modifier = Modifier.padding(PaddingValues(horizontal = Dp24))
            )
        }
    } else {
        Column(
            modifier = modifier
                .background(color = colorScheme.background)
                .padding(horizontal = Dp24)
                .then(if (useSystemBarsPadding) Modifier.systemBarsPadding() else Modifier)
        ) {
            if (hideDefaultTopBar.not()) MeverTopBar(
                topBarArgs = topBarArgs,
                useCenterTopBar = useCenterTopBar
            )
            content()
        }
    }
}