package com.dapascript.mever.core.common.base

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
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor() : ViewModel() {

    fun <T> collectApiAsUiStateWithWorker(
        workManager: WorkManager,
        workerClass: Class<out ListenableWorker>,
        inputData: Data = workDataOf(),
        constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(CONNECTED)
            .build(),
        resetState: Boolean = true,
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
                    SUCCEEDED -> {
                        updateState(StateSuccess(data = transformResponses(workInfo.outputData)))
                        if (resetState) {
                            delay(300)
                            updateState(StateInitial)
                        }
                    }

                    FAILED -> {
                        updateState(
                            StateFailed(message = workInfo.outputData.getString(KEY_ERROR))
                        )
                        if (resetState) {
                            delay(300)
                            updateState(StateInitial)
                        }
                    }

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
        onFailed: (String?) -> Unit = {},
        onReset: (() -> Unit)? = null
    ) {
        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(request)
        viewModelScope.launch {
            workManager.getWorkInfoByIdFlow(request.id).collect { workInfo ->
                when (workInfo?.state) {
                    SUCCEEDED -> {
                        onSuccess(workInfo.outputData)
                        delay(300)
                        onReset?.invoke()
                    }

                    FAILED -> {
                        onFailed(workInfo.outputData.getString(KEY_ERROR))
                        delay(300)
                        onReset?.invoke()
                    }

                    else -> onLoading()
                }
            }
        }
    }

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