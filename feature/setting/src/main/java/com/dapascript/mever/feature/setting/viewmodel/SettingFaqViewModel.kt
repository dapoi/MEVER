package com.dapascript.mever.feature.setting.viewmodel

import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.feature.setting.screen.attr.SettingFaqAttr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingFaqViewModel @Inject constructor() : BaseViewModel() {

    private val _faqList = MutableStateFlow(SettingFaqAttr.faqList)
    val faqList = _faqList.asStateFlow()

    fun onExpand(id: Int) {
        _faqList.update { list ->
            list.map {
                if (it.id == id) it.copy(isExpanded = !it.isExpanded) else it
            }
        }
    }
}
