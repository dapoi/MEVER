package com.dapascript.mever.core.data.worker.base

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ERROR
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_FILE_PATH
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_OUTPUT_IS_FILE
import com.dapascript.mever.core.common.util.worker.WorkerConstant.SIZE_LIMIT
import com.dapascript.mever.core.data.R
import com.dapascript.mever.core.data.util.MoshiHelper
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseWorker<T : Any>(
    protected val context: Context,
    workerParameters: WorkerParameters,
    protected val moshiHelper: MoshiHelper
) : CoroutineWorker(context, workerParameters) {

    abstract val outputSuccessKey: String
    abstract val resultType: Type

    abstract suspend fun doApiCall(): T

    final override suspend fun doWork(): Result = try {
        val resultData = doApiCall()
        if (resultData is Collection<*> && resultData.isEmpty()) {
            Result.failure(
                workDataOf(KEY_ERROR to context.getString(R.string.error_unknown))
            )
        }
        val jsonOutput = moshiHelper.toJson(resultType, resultData)
        val size = jsonOutput?.toByteArray()?.size
        if (size != null && size > SIZE_LIMIT) {
            val path = writeJsonToCache(jsonOutput, outputSuccessKey)
            Result.success(
                workDataOf(
                    KEY_OUTPUT_IS_FILE to true,
                    KEY_OUTPUT_FILE_PATH to path
                )
            )
        } else {
            Result.success(
                workDataOf(
                    KEY_OUTPUT_IS_FILE to false,
                    outputSuccessKey to jsonOutput
                )
            )
        }
    } catch (e: Throwable) {
        val errorMessage = when (e) {
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is UnknownHostException -> context.getString(R.string.error_no_host)
            is IOException -> context.getString(R.string.error_io)
            is HttpException -> context.getString(R.string.error_http, e.code())
            else -> e.message ?: context.getString(R.string.error_unknown)
        }
        Result.failure(workDataOf(KEY_ERROR to errorMessage))
    }

    private fun writeJsonToCache(
        json: String,
        key: String
    ) = context.cacheDir.resolve("$key.json").apply {
        writeText(json)
    }.absolutePath
}