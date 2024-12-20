package com.dapascript.mever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.navigation.MeverNavGraphs
import com.dapascript.mever.core.common.ui.attr.MeverDialogAttr.MeverDialogArgs
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
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
                        HandleConnectionState()
                    }
                }
            }
        }
    }

    @Composable
    private fun HandleConnectionState() {
        var showConnectionModal by remember { mutableStateOf(false) }

        connectivityObserver.observe().collectAsState(connectivityObserver.isConnected()).let { state ->
            (state.value.message != AVAILABLE).let {
                showConnectionModal = it
                ketch.cancelAll()
            }
        }

        MeverDialog(
            showDialog = showConnectionModal,
            meverDialogArgs = MeverDialogArgs(
                title = "Ups! It seems you are offline",
                actionText = null
            )
        ) {
            Text(
                text = "Please check your internet connection and try again",
                style = typography.body1,
                color = colorScheme.onPrimary
            )
        }
    }
}