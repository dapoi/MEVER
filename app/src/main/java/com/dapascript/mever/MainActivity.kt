package com.dapascript.mever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.navigation.MeverNavGraphs
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.util.Constant.ConnectivityName.AVAILABLE
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.navigation.MeverNavHost
import com.ketch.Ketch
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var meverNavGraphs: MeverNavGraphs

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    @Inject
    lateinit var ketch: Ketch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeverTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = colorScheme.background) {
                    CompositionLocalProvider(LocalActivity provides this) {
                        MeverNavHost(meverNavGraphs = meverNavGraphs)

                        // Cancel all downloads when network is not available
                        connectivityObserver.observe().collectAsState(connectivityObserver.isConnected()).let { state ->
                            if (state.value.message != AVAILABLE) ketch.cancelAll()
                        }
                    }
                }
            }
        }
    }
}