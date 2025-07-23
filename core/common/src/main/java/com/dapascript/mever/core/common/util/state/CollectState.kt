package com.dapascript.mever.core.common.util.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun <T> StateFlow<T>.collectAsStateValue() = collectAsState().value