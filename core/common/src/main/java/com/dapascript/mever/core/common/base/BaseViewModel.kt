package com.dapascript.mever.core.common.base

import androidx.compose.runtime.State
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
import kotlinx.coroutines.flow.Flow
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
        onAction: () -> Unit
    ) {
        if (isGranted.not() && showDialogPermission.contains(permission).not()) {
            showDialogPermission.add(permission)
        } else onAction()
    }

    fun <T> collectApiAsUiState(
        flow: Flow<ApiState<T>>,
        updateState: (UiState<T>) -> Unit
    ) {
        viewModelScope.launch(IO) {
            flow.map { response ->
                when (response) {
                    is Success -> StateSuccess(response.data)
                    is Error -> StateFailed(response.throwable)
                    is Loading -> StateLoading
                }
            }.collect { uiState -> updateState(uiState) }
        }
    }

    inline fun <T> State<UiState<T>>.handleUiState(
        crossinline onSuccess: (T) -> Unit,
        crossinline onLoading: () -> Unit,
        crossinline onFailed: (Throwable) -> Unit
    ) {
        when (val state = value) {
            is StateSuccess -> state.data?.let { onSuccess(it) }
            is StateLoading -> onLoading()
            is StateFailed -> onFailed(state.throwable)
            StateInitial -> Unit
        }
    }
}