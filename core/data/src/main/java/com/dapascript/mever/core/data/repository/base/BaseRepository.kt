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
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_FILE_PATH
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_IS_FILE
import com.dapascript.mever.core.data.util.MoshiHelper
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

    inline fun <reified T> WorkManager.collectApiResultWithWorker(
        workerClass: Class<out ListenableWorker>,
        outputKey: String,
        moshiHelper: MoshiHelper,
        requestParam: Data = EMPTY
    ): Flow<ApiState<T>> = channelFlow {
        val request = OneTimeWorkRequest.Builder(workerClass)
            .setInputData(requestParam)
            .build()
        send(ApiState.Loading)
        enqueue(request)

        val job = getWorkInfoByIdFlow(request.id)
            .map { it?.state to it?.outputData }
            .distinctUntilChanged { old, new -> old.first == new.first }
            .onEach { (state, data) ->
                when (state) {
                    SUCCEEDED -> {
                        runCatching { data?.parseWorkerOutput<T>(moshiHelper, outputKey) }
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
            cancelWorkById(request.id)
        }
    }.flowOn(IO)

    inline fun <reified T> Data.parseWorkerOutput(
        moshiHelper: MoshiHelper,
        outputKey: String
    ): T? {
        val isFile = getBoolean(KEY_OUTPUT_IS_FILE, false)
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