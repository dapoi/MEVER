package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16

@Composable
fun MeverSnackbar(
    message: String,
    alignment: Alignment = BottomCenter,
    duration: SnackbarDuration = Short,
    snackbarColor: Color = colorScheme.primary,
    snackbarContentColor: Color = colorScheme.onSecondary,
    modifier: Modifier = Modifier,
    actionMessage: String? = null,
    onResetMessage: (String) -> Unit,
    onClickSnackbar: (() -> Unit)? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message = message, duration = duration)
            onResetMessage("")
        }
    }

    SnackbarHost(hostState = snackbarHostState) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(Dp16),
            contentAlignment = alignment
        ) {
            MeverLabel(
                message = message,
                labelColor = snackbarColor,
                labelContentColor = snackbarContentColor,
                actionMessage = actionMessage
            ) {
                onResetMessage("")
                onClickSnackbar?.invoke()
            }
        }
    }
}