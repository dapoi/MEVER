package com.dapascript.mever.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.state.ApiState.Error
import com.dapascript.mever.core.common.util.state.ApiState.Loading
import com.dapascript.mever.core.common.util.state.ApiState.Success
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

open class BaseViewModel : ViewModel() {
    fun <T> collectApiAsUiState(
        response: Flow<ApiState<T>>,
        state: MutableStateFlow<UiState<T>>
    ) = viewModelScope.launch {
        response.collect { apiState ->
            when (apiState) {
                is Loading -> state.value = StateLoading

                is Success -> {
                    apiState.data?.let {
                        state.value = StateSuccess(it)
                    }

                    delay(300.milliseconds)
                    state.value = StateInitial
                }

                is Error -> {
                    state.value = StateFailed(
                        apiState.throwable.message.orEmpty()
                    )

                    delay(300.milliseconds)
                    state.value = StateInitial
                }
            }
        }
    }

    fun <T> collectApiAsUiState(
        response: Flow<ApiState<T>>,
        onLoading: suspend () -> Unit = {},
        onSuccess: suspend (T) -> Unit = {},
        onFailed: suspend (String) -> Unit = {},
        onReset: (suspend () -> Unit)? = null
    ) = viewModelScope.launch {
        response.collect { apiState ->
            when (apiState) {
                is Loading -> onLoading()
                is Success -> apiState.data?.let { onSuccess(it) }
                is Error -> onFailed(apiState.throwable.message.orEmpty())
            }
            if (apiState is Success || apiState is Error) {
                delay(300.milliseconds)
                onReset?.invoke()
            }
        }
    }

    suspend fun <T> UiState<T>.handleUiState(
        onSuccess: suspend (T) -> Unit = {},
        onLoading: suspend () -> Unit = {},
        onFailed: suspend (String?) -> Unit = {}
    ) = when (this) {
        is StateSuccess -> data?.let { onSuccess(it) }
        is StateLoading -> onLoading()
        is StateFailed -> onFailed(message)
        is StateInitial -> Unit
    }
}