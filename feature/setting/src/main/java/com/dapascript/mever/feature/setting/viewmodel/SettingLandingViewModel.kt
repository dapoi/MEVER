package com.dapascript.mever.feature.setting.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.feature.setting.screen.attr.SettingLandingAttr.getSettingMenus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingLandingViewModel @Inject constructor() : BaseViewModel() {
    val settingMenus by lazy { getSettingMenus() }
    var titleHeight by mutableIntStateOf(0)
        internal set
}