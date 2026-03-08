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
import com.dapascript.mever.core.navigation.helper.createCustomArgs
import com.dapascript.mever.core.navigation.helper.getArgs
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute
import com.dapascript.mever.core.navigation.route.SettingScreenRoute.SettingLanguageRoute.LanguageData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SettingLanguageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @param:ApplicationContext private val context: Context
) : BaseViewModel() {
    val args by lazy {
        savedStateHandle.getArgs<SettingLanguageRoute>(createCustomArgs<LanguageData>())
    }
    val languages by lazy { appLanguages() }

    var titleHeight by mutableIntStateOf(0)
    var getLanguageCode by mutableStateOf(args.languageData.languageCode)

    fun changeLanguage(languageCode: String) {
        LanguageManager.changeLanguage(context, languageCode)
        getLanguageCode = languageCode
    }
}