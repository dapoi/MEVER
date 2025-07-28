package com.dapascript.mever.core.common.util.state

sealed class UiState<out T> {
    data object StateInitial : UiState<Nothing>()
    data class StateSuccess<out T>(val data: T?) : UiState<T>()
    data class StateFailed(val message: String?) : UiState<Nothing>()
    data object StateLoading : UiState<Nothing>()
}