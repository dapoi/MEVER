package com.dapascript.mever.screen

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.SystemBarStyle.Companion.light
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.DeviceType.DESKTOP
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.DeviceType.TABLET
import com.dapascript.mever.core.common.util.InAppUpdateManager
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.navigation.MainNavigation
import com.dapascript.mever.viewmodel.MainViewModel
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.install.model.UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>

    private lateinit var inAppUpdateManager: InAppUpdateManager
    private val viewModel: MainViewModel by viewModels()
    private val updateLauncher = registerForActivityResult(StartIntentSenderForResult()) {
        if (it.resultCode == RESULT_CANCELED) finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        inAppUpdateManager = InAppUpdateManager(this)
        inAppUpdateManager.startUpdate(
            updateAvailability = UPDATE_AVAILABLE,
            launcher = updateLauncher
        )
        enableEdgeToEdge()
        handleShareIntent(intent)
        setContent {
            val themeType = viewModel.themeType.collectAsStateValue()
            val windowSizeClass = calculateWindowSizeClass(this)
            val deviceType = when (windowSizeClass.widthSizeClass) {
                Compact -> PHONE
                Medium -> TABLET
                else -> DESKTOP
            }
            val darkTheme = when (themeType) {
                Light -> false
                Dark -> true
                else -> isSystemInDarkTheme()
            }
            MeverTheme(
                deviceType = deviceType,
                darkTheme = darkTheme
            ) {
                ApplyEdgeToEdgeSystemBars(darkTheme)
                Surface(modifier = Modifier.fillMaxSize(), color = colorScheme.background) {
                    CompositionLocalProvider(LocalActivity provides this) {
                        MainNavigation(
                            navGraphs = navGraphs,
                            deviceType = deviceType,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleShareIntent(intent)
    }

    @Composable
    private fun ApplyEdgeToEdgeSystemBars(darkTheme: Boolean) {
        LaunchedEffect(darkTheme) {
            enableEdgeToEdge(
                statusBarStyle = if (darkTheme) {
                    dark(scrim = MeverDark.toArgb())
                } else {
                    light(
                        scrim = MeverTransparent.toArgb(),
                        darkScrim = MeverDark.toArgb()
                    )
                },
                navigationBarStyle = if (darkTheme) {
                    dark(scrim = MeverDark.toArgb())
                } else {
                    light(
                        scrim = MeverTransparent.toArgb(),
                        darkScrim = MeverDark.toArgb()
                    )
                }
            )
        }
    }

    private fun handleShareIntent(intent: Intent?) {
        if (intent?.action == ACTION_SEND && intent.type == "text/plain") {
            intent.getStringExtra(EXTRA_TEXT)?.let { url ->
                viewModel.saveUrlIntent(url)
                this.intent.action = ""
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inAppUpdateManager.startUpdate(
            updateAvailability = DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS,
            launcher = updateLauncher
        )
    }
}