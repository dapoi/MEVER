package com.dapascript.mever.core.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp12
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.onCustomClick

@Composable
fun MeverSnackbar(
    message: MutableState<String>,
    modifier: Modifier = Modifier,
    actionMessage: String? = null,
    duration: SnackbarDuration = Short,
    snackbarColor: Color = colorScheme.primary,
    snackbarContentColor: Color = colorScheme.onSecondary,
    onClickSnackbarAction: (() -> Unit)? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(message.value) {
        if (message.value.isNotEmpty()) {
            snackbarHostState.showSnackbar(message = message.value, duration = duration)
            message.value = ""
        }
    }

    SnackbarHost(hostState = snackbarHostState) {
        Box(modifier = modifier) {
            MeverLabel(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = snackbarColor, shape = RoundedCornerShape(Dp8)),
                message = message.value,
                messageColor = snackbarContentColor,
                actionMessage = actionMessage
            ) { onClickSnackbarAction?.invoke() }
        }
    }
}

@Composable
private fun MeverLabel(
    message: String,
    messageColor: Color,
    modifier: Modifier = Modifier,
    actionMessage: String? = null,
    onClickAction: () -> Unit
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dp12),
            horizontalArrangement = SpaceBetween
        ) {
            Text(
                text = message,
                style = typography.label1,
                color = messageColor
            )
            actionMessage?.let {
                Text(
                    modifier = Modifier.onCustomClick { onClickAction() },
                    text = actionMessage,
                    style = typography.labelBold1,
                    color = messageColor
                )
            }
        }
    }
}