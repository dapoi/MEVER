package com.dapascript.mever.core.data.repository.base

import android.content.Context
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.data.R
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

open class BaseRepository @Inject constructor(
    open val context: Context
) {
    inline fun <T, R> collectApiResult(
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
}