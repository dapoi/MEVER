package com.dapascript.mever.core.common.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

@Composable
fun rememberDebounceHandler(interval: Long = 500L) = remember { DebounceHandler(interval) }

@Stable
class DebounceHandler(private val interval: Long = 500L) {
    private var lastEventTimeMs = 0L

    fun processClick(event: (() -> Unit)?) {
        val now = System.currentTimeMillis()
        if (now - lastEventTimeMs >= interval) event?.invoke()
        lastEventTimeMs = now
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.onCustomClick(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: (() -> Unit)?
) = composed {
    val handler = rememberDebounceHandler()
    Modifier.combinedClickable(
        enabled = enabled,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() },
        role = role,
        onClickLabel = onClickLabel,
        onLongClick = onLongClick,
        onClick = { handler.processClick(onClick) },
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.onCustomClick(
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClick: (() -> Unit)? = null,
    onClick: (() -> Unit)?
) = composed {
    val handler = rememberDebounceHandler()
    Modifier.combinedClickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = indication,
        role = role,
        onClickLabel = onClickLabel,
        onLongClick = onLongClick,
        onClick = { handler.processClick(onClick) }
    )
}