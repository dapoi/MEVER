package com.dapascript.mever.core.common.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    val showDialogPermission = mutableStateListOf<String>()

    fun dismissDialog() {
        showDialogPermission.removeAt(0)
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
        onAction: () -> Unit = {}
    ) {
        if (isGranted.not() && showDialogPermission.contains(permission).not()) {
            showDialogPermission.add(permission)
        } else onAction()
    }

    fun <T> collectApiAsUiState(
        response: Flow<ApiState<T>>,
        resetState: Boolean = true,
        updateState: (UiState<T>) -> Unit
    ) {
        viewModelScope.launch(IO) {
            response.map {
                when (it) {
                    is Loading -> StateLoading
                    is Success -> StateSuccess(it.data)
                    is Error -> StateFailed(it.throwable)
                }
            }.collect { uiState ->
                updateState(uiState)
                if (resetState && (uiState is StateSuccess || uiState is StateFailed)) {
                    delay(300)
                    updateState(StateInitial)
                }
            }
        }
    }

    fun <T> UiState<T>.handleUiState(
        onSuccess: (T) -> Unit,
        onLoading: () -> Unit,
        onFailed: (Throwable) -> Unit
    ) {
        when (this) {
            is StateSuccess -> data?.let { onSuccess(it) }
            is StateLoading -> onLoading()
            is StateFailed -> onFailed(throwable)
            is StateInitial -> Unit
        }
    }

    @Composable
    fun <T> StateFlow<T>.collectAsStateValue() = collectAsState().value
}