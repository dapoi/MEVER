package com.dapascript.mever.feature.setting.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.common.util.LanguageManager.appLanguages
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    val languages by lazy { appLanguages() }

    val isFirstTimeChangeLanguage = dataStore.isFirstTimeChangeLanguage.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )
    val themeType =dataStore.getTheme.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = System
    )

    fun setIsFirstTimeChangeLanguage(isFirst: Boolean) {
        viewModelScope.launch {
            dataStore.setIsFirstTimeChangeLanguage(isFirst)
        }
    }
}