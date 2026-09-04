package com.dapascript.mever.feature.setting.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.LanguageManager.appLanguages
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_IS_FIRST_CHANGE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SettingLanguageViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {
    val languages by lazy { appLanguages() }

    val isFirstTimeChangeLanguage = repository.getPreference(KEY_IS_FIRST_CHANGE, true).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    fun setIsFirstTimeChangeLanguage(isFirst: Boolean) {
        viewModelScope.launch {
            repository.savePreference(KEY_IS_FIRST_CHANGE, isFirst)
        }
    }
}