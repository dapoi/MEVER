package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemePalette
import com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.util.AppIconManager
import com.dapascript.mever.core.common.util.AppIconType
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_APP_ICON
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_THEME_PALETTE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingThemeViewModel @Inject constructor(
    private val repository: MeverRepository,
    subscriptionRepository: SubscriptionRepository
) : BaseViewModel() {

    val isProActive = subscriptionRepository.isProActive

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

    fun setThemeType(mode: ThemeType) = viewModelScope.launch {
        repository.savePreference(KEY_THEME, mode.name)
    }

    fun setThemePalette(palette: ThemePalette) = viewModelScope.launch {
        repository.savePreference(KEY_THEME_PALETTE, palette.name)
    }

    fun setAppIcon(context: Context, icon: AppIconType) = viewModelScope.launch {
        repository.savePreference(KEY_APP_ICON, icon.name)
        AppIconManager.setAppIcon(context, icon)
    }
}
