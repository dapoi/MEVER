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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    inline fun <T> collectApiResultWithWorker(
        workManager: WorkManager,
        workerClass: Class<out ListenableWorker>,
        requestParam: Data = EMPTY,
        crossinline responses: (Data) -> T
    ): Flow<ApiState<T>> = flow {
        emit(ApiState.Loading)
        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInputData(requestParam)
            .build()
        workManager.enqueue(request)

        workManager.getWorkInfoByIdFlow(request.id).collect { workInfo ->
            when (workInfo?.state) {
                SUCCEEDED -> {
                    val outputData = workInfo.outputData
                    val result = responses(outputData)
                    emit(ApiState.Success(result))
                }

                FAILED, CANCELLED, BLOCKED -> {
                    val error = workInfo.outputData.getString(KEY_ERROR)
                    emit(ApiState.Error(Throwable(error)))
                }

                else -> emit(ApiState.Loading)
            }
        }
    }.flowOn(IO)
}