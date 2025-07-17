package com.dapascript.mever.core.common.base

import androidx.compose.runtime.mutableStateListOf
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
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    private var apiJob: Job? = null
    val showDialogPermission = mutableStateListOf<String>()

    fun <T> collectApiAsUiState(
        response: Flow<ApiState<T>>,
        updateState: (UiState<T>) -> Unit,
        onResetState: (() -> Unit)? = null
    ) {
        apiJob?.cancel()
        apiJob = viewModelScope.launch(IO) {
            response.map { apiState ->
                when (apiState) {
                    is Loading -> StateLoading
                    is Success -> StateSuccess(apiState.data)
                    is Error -> StateFailed(apiState.throwable)
                }
            }.collect { uiState ->
                updateState(uiState)
                if (uiState is StateSuccess || uiState is StateFailed) {
                    delay(300)
                    onResetState?.invoke()
                }
            }
        }
    }

    fun <T> collectApiAsUiState(
        response: Flow<ApiState<T>>,
        onLoading: () -> Unit = {},
        onSuccess: (T) -> Unit = {},
        onFailed: (Throwable) -> Unit = {},
        onReset: (() -> Unit)? = null
    ) {
        apiJob?.cancel()
        apiJob = viewModelScope.launch(IO) {
            response.collect { apiState ->
                when (apiState) {
                    is Loading -> onLoading()
                    is Success -> apiState.data?.let { onSuccess(it) }
                    is Error -> onFailed(apiState.throwable)
                }
                if (apiState is Success || apiState is Error) {
                    delay(300)
                    onReset?.invoke()
                }
            }
        }
    }


    fun <T> UiState<T>.handleUiState(
        onSuccess: (T) -> Unit = {},
        onLoading: () -> Unit = {},
        onFailed: (Throwable) -> Unit = {}
    ) {
        when (this) {
            is StateSuccess -> data?.let { onSuccess(it) }
            is StateLoading -> onLoading()
            is StateFailed -> onFailed(throwable)
            is StateInitial -> Unit
        }
    }

    fun getNetworkStatus(
        isNetworkAvailable: NetworkStatus,
        onNetworkAvailable: () -> Unit,
        onNetworkUnavailable: () -> Unit
    ) = if (isNetworkAvailable == Available) onNetworkAvailable() else onNetworkUnavailable()


    fun dismissDialog() = showDialogPermission.removeRange(0, showDialogPermission.size)

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
        onAction: () -> Unit = {}
    ) {
        if (isGranted.not() && showDialogPermission.contains(permission).not()) {
            showDialogPermission.add(permission)
        } else onAction()
    }

    override fun onCleared() {
        super.onCleared()
        apiJob?.cancel()
    }
}
