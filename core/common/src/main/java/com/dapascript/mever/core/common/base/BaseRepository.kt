package com.dapascript.mever.core.common.base

import com.dapascript.mever.core.common.util.state.ApiState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

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
        } catch (e: Throwable) {
            emit(ApiState.Error(e))
        }
    }.catch { throwable ->
        emit(ApiState.Error(throwable))
    }
}