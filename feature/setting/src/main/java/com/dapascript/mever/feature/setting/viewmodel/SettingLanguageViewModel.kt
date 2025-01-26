package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.LanguageManager.setLanguage
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor(
    private val dataStore: MeverDataStore
) : BaseViewModel() {
    val languages by lazy {
        listOf(
            "English" to "en",
            "Bahasa Indonesia" to "in"
        )
    }
    var titleHeight by mutableIntStateOf(0)

    private val _getLanguageCode = MutableStateFlow("en")
    val getLanguageCode = _getLanguageCode.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.getLanguageCode.collect { _getLanguageCode.value = it }
        }
    }

    fun saveLanguageCode(context: Context, language: String) = viewModelScope.launch {
        setLanguage(context, language)
        dataStore.saveLanguageCode(language)
    }
}