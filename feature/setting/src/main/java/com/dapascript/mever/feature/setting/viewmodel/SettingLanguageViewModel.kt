package com.dapascript.mever.feature.setting.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.LanguageManager.appLanguages
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.navigation.helper.createCustomArgs
import com.dapascript.mever.core.navigation.helper.getArgs
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute.LanguageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    val isFirstTimeChangeLanguage = dataStore.isFirstTimeChangeLanguage.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )
    val args by lazy {
        savedStateHandle.getArgs<SettingLanguageRoute>(createCustomArgs<LanguageData>())
    }
    val languages by lazy { appLanguages() }

    var titleHeight by mutableIntStateOf(0)
    var languageCode by mutableStateOf(args.languageData.languageCode)

    fun setIsFirstTimeChangeLanguage(isFirst: Boolean) {
        viewModelScope.launch {
            dataStore.setIsFirstTimeChangeLanguage(isFirst)
        }
    }
}