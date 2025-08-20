package com.dapascript.mever.feature.setting.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.LanguageManager
import com.dapascript.mever.core.common.util.LanguageManager.appLanguages
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @param:ApplicationContext private val context: Context,
) : BaseViewModel() {
    val args by lazy { SettingLanguageRoute.getArgs(savedStateHandle) }
    val languages by lazy { appLanguages() }

    var titleHeight by mutableIntStateOf(0)
    var getLanguageCode by mutableStateOf(args.languageData.languageCode)

    fun changeLanguage(languageCode: String) {
        LanguageManager.changeLanguage(context, languageCode)
        getLanguageCode = languageCode
    }
}