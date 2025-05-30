package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
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

@Composable
fun MeverSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionMessage: String? = null,
    alignment: Alignment = BottomCenter,
    duration: SnackbarDuration = Short,
    snackbarColor: Color = colorScheme.primary,
    snackbarContentColor: Color = colorScheme.onSecondary,
    onClickSnackbar: (() -> Unit)? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message = message, duration = duration)
        }
    }

    SnackbarHost(hostState = snackbarHostState) {
        Box(
            modifier = modifier,
            contentAlignment = alignment
        ) {
            MeverLabel(
                message = message,
                labelColor = snackbarColor,
                labelContentColor = snackbarContentColor,
                actionMessage = actionMessage
            ) { onClickSnackbar?.invoke() }
        }
    }
}