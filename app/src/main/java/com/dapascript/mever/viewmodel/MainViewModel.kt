package com.dapascript.mever.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.BuildConfig.VERSION_NAME
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemePalette
import com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.AppIconType
import com.dapascript.mever.core.data.deeplink.DeeplinkManager
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_APP_ICON
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_LAST_INTERACTION_TIME
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_QUICK_NOTIF
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME_PALETTE
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_VERSION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val repository: MeverRepository,
    private val deeplinkManager: DeeplinkManager,
    private val subscriptionRepository: SubscriptionRepository
) : BaseViewModel() {

    val themeType = repository.getPreference(KEY_THEME, ThemeType.System.name).map { name ->
        try {
            ThemeType.valueOf(name)
        } catch (_: IllegalArgumentException) {
            ThemeType.System
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = ThemeType.System
    )

    val themePalette = repository.getPreference(KEY_THEME_PALETTE, CLASSIC.name).map { name ->
        try {
            ThemePalette.valueOf(name)
        } catch (_: IllegalArgumentException) {
            CLASSIC
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = CLASSIC
    )

    val appIcon = repository.getPreference(KEY_APP_ICON, AppIconType.Default.name).map { name ->
        try {
            AppIconType.valueOf(name)
        } catch (_: IllegalArgumentException) {
            AppIconType.Default
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = AppIconType.Default
    )

    val isProActive = subscriptionRepository.isProActive
    val isEntitlementChecked = subscriptionRepository.isEntitlementChecked

    val isQuickNotifEnabled = repository.getPreference(KEY_QUICK_NOTIF, false).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = false
    )

    private val _navDeeplinkTrigger = Channel<Unit>(capacity = Channel.CONFLATED)
    val navDeeplinkTrigger = _navDeeplinkTrigger.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.savePreference(KEY_VERSION, VERSION_NAME)
        }
        viewModelScope.launch {
            isProActive.collect { isPro ->
                if (!isPro) {
                    val currentPalette = repository.getPreference(KEY_THEME_PALETTE, CLASSIC.name).first()
                    try {
                        if (ThemePalette.valueOf(currentPalette).isProOnly) {
                            repository.savePreference(KEY_THEME_PALETTE, CLASSIC.name)
                        }
                    } catch (_: IllegalArgumentException) {
                        repository.savePreference(KEY_THEME_PALETTE, CLASSIC.name)
                    }

                    val currentIcon = repository.getPreference(KEY_APP_ICON, AppIconType.Default.name).first()
                    try {
                        if (AppIconType.valueOf(currentIcon).isProOnly) {
                            repository.savePreference(KEY_APP_ICON, AppIconType.Default.name)
                        }
                    } catch (_: IllegalArgumentException) {
                        repository.savePreference(KEY_APP_ICON, AppIconType.Default.name)
                    }
                }
            }
        }
    }

    fun handleDeeplink(uri: Uri, isTriggerNavigation: Boolean = true) {
        viewModelScope.launch {
            deeplinkManager.handleDeeplink(uri)
            if (isTriggerNavigation) {
                _navDeeplinkTrigger.send(Unit)
            }
        }
    }

    fun checkSubscriptionEntitlement() {
        subscriptionRepository.checkEntitlement()
    }

    fun updateInteractionTime() {
        viewModelScope.launch {
            repository.savePreference(KEY_LAST_INTERACTION_TIME, System.currentTimeMillis())
        }
    }

    fun resetInteractionTime() {
        viewModelScope.launch {
            repository.savePreference(KEY_LAST_INTERACTION_TIME, 0L)
        }
    }

    fun checkIdleTimeout(onTimeout: () -> Unit) {
        viewModelScope.launch {
            val lastTime = repository.getPreference(KEY_LAST_INTERACTION_TIME, 0L).first()
            val threeMinutes = 3 * 60 * 1000L
            val currentTime = System.currentTimeMillis()
            if ((lastTime != 0L) && ((currentTime - lastTime) > threeMinutes)) {
                onTimeout()
            }
            updateInteractionTime()
        }
    }
}