package com.dapascript.mever.core.data.repository.base

import androidx.work.Data
import androidx.work.Data.Companion.EMPTY
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo.State.BLOCKED
import androidx.work.WorkInfo.State.CANCELLED
import androidx.work.WorkInfo.State.FAILED
import androidx.work.WorkInfo.State.SUCCEEDED
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ACTION
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_FILE_PATH
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_IS_FILE
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESULT
import com.dapascript.mever.core.data.R
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.worker.MeverWorker
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseRepository(
    private val args: BaseRepositoryArgs
) {

    @PublishedApi
    internal val context get() = args.context

    @PublishedApi
    internal val workManager get() = args.workManager

    @PublishedApi
    internal val moshiHelper get() = args.moshiHelper

    fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Flow<ApiState<T>> = flow {
        emit(ApiState.Loading)
        try {
            emit(ApiState.Success(apiCall()))
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is SocketTimeoutException -> context.getString(R.string.error_timeout)
                is UnknownHostException -> context.getString(R.string.error_no_host)
                is IOException -> context.getString(R.string.error_io)
                is HttpException -> context.getString(R.string.error_http, e.code())
                else -> e.message ?: context.getString(R.string.error_unknown)
            }
            emit(ApiState.Error(Throwable(errorMessage)))
        }
    }.flowOn(IO)

    inline fun <reified T> safeWorkerCall(
        serviceType: String,
        requestParam: Data = EMPTY
    ): Flow<ApiState<T>> = channelFlow {
        val inputData = Data.Builder()
            .putAll(requestParam)
            .putString(KEY_ACTION, serviceType)
            .build()

        val request = OneTimeWorkRequest.Builder(MeverWorker::class.java)
            .setInputData(inputData)
            .build()
        send(ApiState.Loading)
        workManager.enqueue(request)

        val job = workManager.getWorkInfoByIdFlow(request.id)
            .map { it?.state to it?.outputData }
            .distinctUntilChanged { old, new -> old.first == new.first }
            .onEach { (state, data) ->
                when (state) {
                    SUCCEEDED -> {
                        runCatching { data?.parseWorkerOutput<T>(moshiHelper, KEY_RESULT) }
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

    inline fun <reified T> Data.parseWorkerOutput(
        moshiHelper: MoshiHelper,
        outputKey: String
    ): T? {
        val isFile = getBoolean(KEY_OUTPUT_IS_FILE, defaultValue = false)
        val json = if (isFile.not()) {
            getString(outputKey) ?: return null
        } else {
            val path = getString(KEY_OUTPUT_FILE_PATH) ?: return null
            val file = java.io.File(path)
            val text = runCatching { file.readText() }.getOrNull() ?: return null
            runCatching { file.delete() }
            text
        }
        return moshiHelper.fromJson<T>(json)
    }
}