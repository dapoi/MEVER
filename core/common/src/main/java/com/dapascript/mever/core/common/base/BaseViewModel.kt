package com.dapascript.mever.core.common.base

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.SUCCEEDED
import androidx.work.WorkManager
import androidx.work.workDataOf
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

    val showDialogPermission = mutableStateListOf<String>()

    fun <T> collectApiAsUiStateWithWorker(
        workManager: WorkManager,
        workerClass: Class<out ListenableWorker>,
        inputData: Data = workDataOf(),
        constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(CONNECTED)
            .build(),
        updateState: (UiState<T>) -> Unit,
        transformResponses: (Data) -> T
    ) {
        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(request)
        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(request.id).collect { workInfo ->
                when (workInfo?.state) {
                    SUCCEEDED -> updateState(StateSuccess(data = transformResponses(workInfo.outputData)))
                    FAILED -> updateState(StateFailed(throwable = Throwable()))
                    else -> updateState(StateLoading)
                }
            }
        }
    }

    fun collectApiAsUiStateWithWorker(
        workManager: WorkManager,
        workerClass: Class<out ListenableWorker>,
        inputData: Data = workDataOf(),
        constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(CONNECTED)
            .build(),
        onLoading: () -> Unit = {},
        onSuccess: (Data) -> Unit = {},
        onFailed: (Throwable) -> Unit = {}
    ) {
        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(request)
        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(request.id).collect { workInfo ->
                when (workInfo?.state) {
                    SUCCEEDED -> onSuccess(workInfo.outputData)
                    FAILED -> onFailed(Throwable())
                    else -> onLoading()
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
    ) = viewModelScope.launch {
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
}