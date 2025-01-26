package com.dapascript.mever

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    private val _getLanguage = MutableStateFlow("en")
    val getLanguage = _getLanguage.asStateFlow()

    private val _themeType = MutableStateFlow(System)
    val themeType = _themeType.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                dataStore.getLanguageCode,
                dataStore.getTheme
            ) { language, theme ->
                _getLanguage.value = language
                _themeType.value = theme
            }.collect()
        }
    }
}