package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.LanguageManager.setLanguage
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.navigation.extension.generateCustomNavType
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingLanguageRoute
import com.dapascript.mever.feature.setting.navigation.route.SettingRoutes.SettingLanguageRoute.LanguageData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor(
    private val dataStore: MeverDataStore,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val args by lazy { savedStateHandle.toRoute<SettingLanguageRoute>(mapOf(generateCustomNavType<LanguageData>())) }
    val languages by lazy {
        listOf(
            "English" to "en",
            "Bahasa Indonesia" to "in"
        )
    }
    var titleHeight by mutableIntStateOf(0)

    fun saveLanguageCode(context: Context, language: String) = viewModelScope.launch {
        setLanguage(context, language)
        dataStore.saveLanguageCode(language)
    }
}