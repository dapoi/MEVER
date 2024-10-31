package com.dapascript.mever.core.common.util

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
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

fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: (() -> Unit)?
) = composed {
    val handler = rememberDebounceHandler()
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { handler.processClick(onClick) },
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }
    )
}

fun Modifier.clickableSingle(
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: (() -> Unit)?
) = composed {
    val handler = rememberDebounceHandler()
    Modifier.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
    ) { handler.processClick(onClick) }
}