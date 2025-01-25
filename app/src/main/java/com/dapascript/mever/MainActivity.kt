package com.dapascript.mever

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.dapascript.mever.core.common.navigation.MeverNavGraphs
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.navigation.MeverNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var meverNavGraphs: MeverNavGraphs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeType = mainViewModel.themeType.collectAsState()
            MeverTheme(
                darkTheme = when (themeType.value) {
                    Light -> false
                    Dark -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = colorScheme.background) {
                    CompositionLocalProvider(LocalActivity provides this) {
                        MeverNavHost(meverNavGraphs = meverNavGraphs)
                    }
                }
            }
        }
    }
}