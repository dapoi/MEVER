package com.dapascript.mever.core.common.util.connectivity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Available
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Losing
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Lost
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Unavailable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("MissingPermission")
class ConnectivityObserverImpl @Inject constructor(
    application: Application
) : ConnectivityObserver {

    private val connectivityManager = application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe() = callbackFlow {
        val networkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(Available) }
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                launch { send(Losing) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(Lost) }
            }

            override fun onUnavailable() {
                super.onUnavailable()
                launch { send(Unavailable) }
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        awaitClose { connectivityManager.unregisterNetworkCallback(networkCallback) }
    }.distinctUntilChanged()

    override fun isConnected() = if (connectivityManager.activeNetwork != null &&
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) != null
    ) Available else Unavailable
}