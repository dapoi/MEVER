package com.dapascript.mever.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.dapascript.mever.core.common.ui.theme.MeverTheme
import com.dapascript.mever.core.common.ui.theme.ThemeType.Dark
import com.dapascript.mever.core.common.ui.theme.ThemeType.Light
import com.dapascript.mever.core.common.util.InAppUpdateManager
import com.dapascript.mever.core.common.util.LanguageManager.setLanguage
import com.dapascript.mever.core.common.util.LocalActivity
import com.dapascript.mever.core.navigation.base.BaseNavGraph
import com.dapascript.mever.navigation.MainNavigation
import com.dapascript.mever.viewmodel.MainViewModel
import com.google.android.play.core.install.model.UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        inAppUpdateManager = InAppUpdateManager(this)
        inAppUpdateManager.startUpdate(
            updateAvailability = UPDATE_AVAILABLE,
            launcher = updateLauncher
        )
        enableEdgeToEdge()
        setLanguage()
        setContent {
            val themeType = viewModel.themeType.collectAsState()
            MeverTheme(
                darkTheme = when (themeType.value) {
                    Light -> false
                    Dark -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = colorScheme.background) {
                    CompositionLocalProvider(LocalActivity provides this) {
                        MainNavigation(navGraphs = navGraphs)
                    }
                }
            }
        }
    }

    private fun setLanguage() = lifecycleScope.launch {
        viewModel.getLanguage.collect { languageCode ->
            setLanguage(
                this@MainActivity,
                languageCode
            )
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