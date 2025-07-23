package com.dapascript.mever.core.common.base

import com.dapascript.mever.core.common.util.state.ApiState
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
        emit(ApiState.Loading)
        try {
            val response = fetchApi()
            val mappedData = transformData(response)
            emit(ApiState.Success(mappedData))
        } catch (e: SocketTimeoutException) {
            emit(ApiState.Error(e))
        } catch (e: SocketException) {
            emit(ApiState.Error(e))
        } catch (e: IOException) {
            emit(ApiState.Error(e))
        } catch (e: Throwable) {
            emit(ApiState.Error(e))
        }
    }.flowOn(IO)
}