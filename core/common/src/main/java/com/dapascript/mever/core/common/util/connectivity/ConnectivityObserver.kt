package com.dapascript.mever.core.common.util.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<NetworkStatus>

    fun isConnected(): NetworkStatus

    enum class NetworkStatus {
        Available,
        Unavailable,
        Losing,
        Lost
    }
}