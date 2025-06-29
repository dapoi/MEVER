package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.LanguageManager.changeLanguage
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor(
    private val dataStore: MeverDataStore,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val args by lazy { SettingLanguageRoute.getArgs(savedStateHandle) }
    val languages by lazy {
        listOf(
            "English" to "en",
            "Bahasa Indonesia" to "in"
        )
    }
    var titleHeight by mutableIntStateOf(0)

    fun saveLanguageCode(context: Context, languageCode: String) = viewModelScope.launch {
        changeLanguage(context = context, languageCode = languageCode)
        dataStore.saveLanguageCode(languageCode)
    }
}