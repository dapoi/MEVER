package com.dapascript.mever.core.data.repository.base

import androidx.work.Data
import androidx.work.Data.Companion.EMPTY
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo.State.BLOCKED
import androidx.work.WorkInfo.State.CANCELLED
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.SUCCEEDED
import androidx.work.WorkManager
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    inline fun <T> collectApiResultWithWorker(
        workManager: WorkManager,
        workerClass: Class<out ListenableWorker>,
        requestParam: Data = EMPTY,
        crossinline responses: (Data) -> T
    ): Flow<ApiState<T>> = channelFlow {
        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInputData(requestParam)
            .build()
        send(ApiState.Loading)
        workManager.enqueue(request)

        val job = workManager.getWorkInfoByIdFlow(request.id)
            .map { it?.state to it?.outputData }
            .distinctUntilChanged { old, new -> old.first == new.first }
            .onEach { (state, data) ->
                when (state) {
                    SUCCEEDED -> {
                        runCatching { data?.let { responses(it) } }
                            .onSuccess { send(ApiState.Success(it)) }
                            .onFailure { send(ApiState.Error(it)) }
                        close()
                    }

                    FAILED, CANCELLED, BLOCKED -> {
                        val error = data?.getString(KEY_ERROR)
                        send(ApiState.Error(Throwable(error)))
                        close()
                    }

                    else -> send(ApiState.Loading)
                }
            }
            .launchIn(this)
        awaitClose {
            job.cancel()
            workManager.cancelWorkById(request.id)
        }
    }.flowOn(IO)
}