package com.dapascript.mever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.navigation.MeverNavGraphs
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.navigation.MeverNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var meverNavGraphs: MeverNavGraphs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeverTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MeverNavHost(
                        meverNavGraphs = meverNavGraphs,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}