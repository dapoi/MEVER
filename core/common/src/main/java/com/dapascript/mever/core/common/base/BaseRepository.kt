package com.dapascript.mever.core.common.base

import com.dapascript.mever.core.common.util.state.ApiState.Error
import com.dapascript.mever.core.common.util.state.ApiState.Loading
import com.dapascript.mever.core.common.util.state.ApiState.Success
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException

open class BaseRepository {
    inline fun <T, R> collectApiResult(
        crossinline fetchApi: suspend () -> T,
        crossinline transformData: (T) -> R
    ) = flow {
        emit(Loading)
        try {
            val response = fetchApi()
            val mappedData = transformData(response)
            emit(Success(mappedData))
        } catch (e: Throwable) {
            when(e) {
                is SocketException, is SocketTimeoutException -> {
                    emit(Error(Throwable("Network error, please check your connection.")))
                }
                is IOException -> {
                    emit(Error(Throwable("I/O error occurred.")))
                }
                else -> {
                    emit(Error(Throwable("An unexpected error occurred: ${e.message}")))
                }
            }
        }
    }.flowOn(IO)
}