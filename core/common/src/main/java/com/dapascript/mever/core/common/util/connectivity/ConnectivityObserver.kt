package com.dapascript.mever.core.common.util.connectivity

import com.dapascript.mever.core.common.util.Constant.ConnectivityName.AVAILABLE
import com.dapascript.mever.core.common.util.Constant.ConnectivityName.LOSING
import com.dapascript.mever.core.common.util.Constant.ConnectivityName.LOST
import com.dapascript.mever.core.common.util.Constant.ConnectivityName.UNAVAILABLE
import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    fun isConnected(): Status

    enum class Status(val message: String) {
        Available(AVAILABLE),
        Unavailable(UNAVAILABLE),
        Losing(LOSING),
        Lost(LOST)
    }
}