package com.dapascript.mever.feature.setting.viewmodel

import com.dapascript.mever.core.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingLandingViewModel @Inject constructor() : BaseViewModel() {

    val settingMenus = listOf(
        "Dark Mode"
    )
}