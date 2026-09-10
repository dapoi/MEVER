package com.dapascript.mever.screen

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_TEXT
import android.os.Bundle
import androidx.activity.SystemBarStyle.Companion.dark
import androidx.activity.SystemBarStyle.Companion.light
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.lifecycleScope
import com.dapascript.mever.BuildConfig.ADMOB_ID
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.DeviceType.DESKTOP
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.DeviceType.TABLET
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.PATH_HOME
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import com.dapascript.mever.core.navigation.MainNavigation
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.viewmodel.MainViewModel
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupAdmob()
        handleShareIntent(intent = intent, isTriggerNavigation = false)

        val action = intent.action
        val type = intent.type
        val initialPath = when (action) {
            ACTION_VIEW -> intent.data?.path
            ACTION_SEND if type == "text/plain" -> PATH_HOME
            else -> null
        }

        setContent {
            val themeType = viewModel.themeType.collectAsStateValue()
            val windowSizeClass = calculateWindowSizeClass(this)
            val deviceType = remember(windowSizeClass) {
                when (windowSizeClass.widthSizeClass) {
                    Compact -> PHONE
                    Medium -> TABLET
                    else -> DESKTOP
                }
            }
            val isDarkMode = when (themeType) {
                Light -> false
                Dark -> true
                else -> isSystemInDarkTheme()
            }

            SideEffect {
                enableEdgeToEdge(
                    statusBarStyle = if (isDarkMode) {
                        dark(scrim = MeverTransparent.toArgb())
                    } else {
                        light(
                            scrim = MeverTransparent.toArgb(),
                            darkScrim = MeverDark.toArgb()
                        )
                    },
                    navigationBarStyle = if (isDarkMode) {
                        dark(scrim = MeverTransparent.toArgb())
                    } else {
                        light(
                            scrim = MeverTransparent.toArgb(),
                            darkScrim = MeverDark.toArgb()
                        )
                    }
                )
            }

            MeverTheme(
                deviceType = deviceType,
                isDarkMode = isDarkMode
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = colors.whiteDark) {
                    CompositionLocalProvider(
                        LocalActivity provides this,
                        LocalDeviceType provides deviceType
                    ) {
                        MainNavigation(
                            navGraphs = navGraphs,
                            navigationEvent = viewModel.navigationEvent,
                            initialPath = initialPath
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleShareIntent(intent = intent, isTriggerNavigation = true)
    }

    private fun setupAdmob() {
        lifecycleScope.launch {
            MobileAds.initialize(
                this@MainActivity,
                InitializationConfig.Builder(ADMOB_ID).build()
            )
            Timber.d("AdMob initialized")
        }
    }

    private fun handleShareIntent(intent: Intent?, isTriggerNavigation: Boolean) {
        when (intent?.action) {
            ACTION_SEND if intent.type == "text/plain" -> {
                intent.getStringExtra(EXTRA_TEXT)?.let { link ->
                    viewModel.saveLinkContent(link, isTriggerNavigation)
                    this.intent.action = ""
                }
            }

            ACTION_VIEW -> {
                intent.data?.let { uri ->
                    viewModel.handleDeeplink(uri, isTriggerNavigation)
                    this.intent.action = ""
                }
            }
        }
    }
}