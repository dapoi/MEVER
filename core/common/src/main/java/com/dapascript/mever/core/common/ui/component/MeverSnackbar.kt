package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration.Long
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16

@Composable
fun MeverSnackbar(
    message: String,
    alignment: Alignment = BottomCenter,
    modifier: Modifier = Modifier,
    onResetMessage: (String) -> Unit,
    onClickSnackbar: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message = message, duration = Long)
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
                actionMessage = "View"
            ) {
                onResetMessage("")
                onClickSnackbar()
            }
        }
    }
}