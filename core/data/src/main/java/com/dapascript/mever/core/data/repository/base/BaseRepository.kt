package com.dapascript.mever.core.data.repository.base

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.Data.Companion.EMPTY
import androidx.work.ListenableWorker
import androidx.work.NetworkType.CONNECTED
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.SUCCEEDED
import androidx.work.WorkManager
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.data.R
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    inline fun <T, R> collectApiResult(
        context: Context,
        crossinline fetchApi: suspend () -> T,
        crossinline transformData: (T) -> R
    ) = flow {
        emit(ApiState.Loading)
        try {
            val response = fetchApi()
            val mappedData = transformData(response)
            emit(ApiState.Success(mappedData))
        } catch (e: Throwable) {
            val errorMessage = when (e) {
                is SocketTimeoutException -> context.getString(R.string.error_timeout)
                is UnknownHostException -> context.getString(R.string.error_no_host)
                is IOException -> context.getString(R.string.error_io)
                is HttpException -> context.getString(R.string.error_http, e.code())
                else -> context.getString(R.string.error_unknown)
            }
            emit(ApiState.Error(Throwable(errorMessage)))
        }
    }.flowOn(IO)

    inline fun <T> collectApiResultWithWorker(
        workManager: WorkManager,
        workerClass: Class<out ListenableWorker>,
        inputData: Data = EMPTY,
        constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(CONNECTED)
            .build(),
        crossinline output: (Data) -> T
    ): Flow<ApiState<T>> = flow {
        emit(ApiState.Loading)
        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()
        workManager.enqueue(request)

        workManager.getWorkInfoByIdFlow(request.id).collect { workInfo ->
            when (workInfo?.state) {
                SUCCEEDED -> {
                    val outputData = workInfo.outputData
                    val result = output(outputData)
                    emit(ApiState.Success(result))
                }

                FAILED -> {
                    val error = workInfo.outputData.getString(KEY_ERROR)
                    emit(ApiState.Error(Throwable(error)))
                }

                else -> emit(ApiState.Loading)
            }
        }
    }.flowOn(IO)
}