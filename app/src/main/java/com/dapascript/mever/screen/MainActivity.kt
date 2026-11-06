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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.dapascript.mever.BuildConfig.ADMOB_ID
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.component.MeverDialog
import com.dapascript.mever.core.common.ui.theme.MeverDark
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.MeverThemeAttr.colors
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.AppIconManager.isProIconActive
import com.dapascript.mever.core.common.util.AppIconManager.resetToDefaultIfProOnly
import com.dapascript.mever.core.common.util.DeviceType.DESKTOP
import com.dapascript.mever.core.common.util.DeviceType.PHONE
import com.dapascript.mever.core.common.util.DeviceType.TABLET
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.common.util.LocalDeviceType
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Path.HOME
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.URL
import com.dapascript.mever.core.common.util.deeplink.buildMeverDeeplink
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.cancelQuickDownloadNotification
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.requestTileUpdate
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.showQuickDownloadNotification
import com.dapascript.mever.core.common.util.recreateActivity
import com.dapascript.mever.core.common.util.state.collectAsStateValue
import kotlinx.coroutines.flow.combine
import com.dapascript.mever.core.navigation.MainNavigation
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.viewmodel.MainViewModel
import com.google.android.libraries.ads.mobile.sdk.MobileAds
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navGraphs: Set<@JvmSuppressWildcards BaseNavGraph>

    private val viewModel: MainViewModel by viewModels()
    private var lastInteractionCheck = 0L
    private var hasIncomingDeeplink = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupAdmob()
        hasIncomingDeeplink = setupIntent(intent = intent, isTriggerNavigation = false)

        setContent {
            val themeType = viewModel.themeType.collectAsStateValue()
            val themePalette = viewModel.themePalette.collectAsStateValue()
            val isQuickNotifEnabled = viewModel.isQuickNotifEnabled.collectAsStateValue()
            val isProActive = viewModel.isProActive.collectAsStateValue()
            var showProExpiredDialog by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                combine(viewModel.isEntitlementChecked, viewModel.isProActive) { checked, isPro ->
                    checked && isPro.not()
                }.collect { shouldReset ->
                    if (shouldReset && isProIconActive(this@MainActivity)) {
                        showProExpiredDialog = true
                    }
                }
            }

            LaunchedEffect(isQuickNotifEnabled, isProActive) {
                if (isQuickNotifEnabled && isProActive) {
                    showQuickDownloadNotification(this@MainActivity)
                } else {
                    cancelQuickDownloadNotification(this@MainActivity)
                }
                requestTileUpdate(this@MainActivity)
            }

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
            val activePalette = remember(themePalette, isProActive) {
                if (isProActive || themePalette.isProOnly.not()) themePalette
                else CLASSIC
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
                isDarkMode = isDarkMode,
                themePalette = activePalette
            ) {
                MeverDialog(
                    showDialog = showProExpiredDialog,
                    image = null,
                    title = stringResource(R.string.pro_expired_title),
                    description = stringResource(R.string.pro_expired_desc),
                    primaryActionLabel = stringResource(R.string.ok),
                    secondaryActionLabel = null,
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false,
                    onClickPrimaryAction = {
                        showProExpiredDialog = false
                        resetToDefaultIfProOnly(this@MainActivity)
                    }
                )

                Surface(modifier = Modifier.fillMaxSize(), color = colors.whiteDark) {
                    CompositionLocalProvider(
                        LocalActivity provides this,
                        LocalDeviceType provides deviceType
                    ) {
                        MainNavigation(
                            navGraphs = navGraphs,
                            navDeeplinkTrigger = viewModel.navDeeplinkTrigger
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        setupIntent(intent = intent, isTriggerNavigation = true)
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkSubscriptionEntitlement()
        if (hasIncomingDeeplink) {
            hasIncomingDeeplink = false
            viewModel.updateInteractionTime()
        } else {
            viewModel.checkIdleTimeout { recreateActivity(context = this, activity = this) }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateInteractionTime()
    }

    override fun onDestroy() {
        viewModel.resetInteractionTime()
        super.onDestroy()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastInteractionCheck > 1000L) {
            lastInteractionCheck = currentTime
            viewModel.updateInteractionTime()
        }
    }

    private fun setupAdmob() {
        lifecycleScope.launch {
            MobileAds.initialize(
                this@MainActivity,
                InitializationConfig.Builder(ADMOB_ID).build()
            )
        }
    }

    private fun setupIntent(intent: Intent?, isTriggerNavigation: Boolean): Boolean {
        val action = intent?.action
        val type = intent?.type
        var isDeeplinkHandled = false

        when (action) {
            ACTION_SEND if type == "text/plain" -> {
                intent.getStringExtra(EXTRA_TEXT)?.let { url ->
                    val deepLink = buildMeverDeeplink(
                        path = HOME,
                        params = mapOf(URL to url)
                    )
                    viewModel.handleDeeplink(deepLink.toUri(), isTriggerNavigation)
                    isDeeplinkHandled = true
                }
            }

            ACTION_VIEW -> intent.data?.let { uri ->
                viewModel.handleDeeplink(uri, isTriggerNavigation)
                isDeeplinkHandled = true
            }
        }

        action?.let { intent.action = "" }
        return isDeeplinkHandled
    }
}