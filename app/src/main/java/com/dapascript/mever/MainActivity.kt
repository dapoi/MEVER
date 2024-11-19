package com.dapascript.mever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
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
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp16
import com.dapascript.mever.core.common.ui.theme.Dimens.Dp8
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.MeverTheme.typography
import com.dapascript.mever.core.common.util.Constant.ConnectivityName.AVAILABLE
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.navigation.MeverNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var meverNavGraphs: MeverNavGraphs

    @Inject
    lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeverTheme {
                CompositionLocalProvider(LocalActivity provides this) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        MeverNavHost(
                            meverNavGraphs = meverNavGraphs,
                            modifier = Modifier.padding(innerPadding)
                        )
                        HandleConnectionState()
                    }
                }
            }
        }
    }

    @Composable
    private fun HandleConnectionState(modifier: Modifier = Modifier) {
        var showConnectionModal by remember { mutableStateOf(false) }

        connectivityObserver.observe().collectAsState(connectivityObserver.isConnected()).let { state ->
            showConnectionModal = state.value.message != AVAILABLE
        }

        MeverDialog(
            modifier = modifier,
            showDialog = showConnectionModal,
            onDismiss = {}
        ) {
            Column(
                modifier = Modifier
                    .background(colorScheme.surface)
                    .padding(horizontal = Dp16, vertical = Dp8),
                verticalArrangement = spacedBy(Dp8)
            ) {
                Text(
                    text = "Internet connection is not available",
                    style = typography.bodyBold1
                )
                Text(
                    text = "Please check your internet connection and try again",
                    style = typography.body1
                )
            }
        }
    }
}