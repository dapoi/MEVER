package com.dapascript.mever.feature.setting.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingLandingViewModel @Inject constructor() : BaseViewModel() {
    var titleHeight by mutableIntStateOf(0)
        internal set
    val settingMenus = listOf(
        "App" to listOf(
            "Language",
            "Notification",
            "Dark Mode"
        ),
        "Donate" to listOf(
            "Paypal",
            "Trakteer"
        )
    )
}