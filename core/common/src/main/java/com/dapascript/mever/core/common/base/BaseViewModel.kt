package com.dapascript.mever.core.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Available
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.state.ApiState.Error
import com.dapascript.mever.core.common.util.state.ApiState.Loading
import com.dapascript.mever.core.common.util.state.ApiState.Success
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    fun <T> collectApiAsUiState(
        response: Flow<ApiState<T>>,
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
        onFailed: (String) -> Unit = {},
        onReset: (() -> Unit)? = null
    ) = viewModelScope.launch {
        response.collect { apiState ->
            when (apiState) {
                is Loading -> onLoading()
                is Success -> apiState.data?.let { onSuccess(it) }
                is Error -> onFailed(apiState.throwable.message.orEmpty())
            }
            if (apiState is Success || apiState is Error) {
                delay(300)
                onReset?.invoke()
            }
        }
    }

    fun <T> UiState<T>.handleUiState(
        onSuccess: (T) -> Unit = {},
        onLoading: () -> Unit = {},
        onFailed: (String?) -> Unit = {}
    ) = when (this) {
        is StateSuccess -> data?.let { onSuccess(it) }
        is StateLoading -> onLoading()
        is StateFailed -> onFailed(message)
        is StateInitial -> Unit
    }

    fun getNetworkStatus(
        isNetworkAvailable: NetworkStatus,
        onNetworkAvailable: () -> Unit,
        onNetworkUnavailable: () -> Unit
    ) = if (isNetworkAvailable == Available) onNetworkAvailable() else onNetworkUnavailable()
}