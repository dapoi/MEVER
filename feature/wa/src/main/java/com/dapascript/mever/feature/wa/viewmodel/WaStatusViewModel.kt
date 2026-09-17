package com.dapascript.mever.feature.wa.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.data.model.local.WaStatusEntity
import com.dapascript.mever.core.data.model.local.WaType
import com.dapascript.mever.core.data.repository.MeverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class WaStatusViewModel @Inject constructor(
    private val meverRepository: MeverRepository
) : BaseViewModel() {
    private val _waStatuses = MutableStateFlow<List<WaStatusEntity>?>(null)
    val waStatuses = _waStatuses.asStateFlow()

    private val statusMap = mutableMapOf<WaType, List<WaStatusEntity>>()

    fun fetchStatuses(folderUri: Uri, type: WaType) {
        viewModelScope.launch {
            val result = meverRepository.fetchWhatsAppStatuses(folderUri, type)
            statusMap[type] = result
            updateStatuses()
        }
    }

    fun onFetchFinished() {
        if (_waStatuses.value == null) {
            _waStatuses.value = emptyList()
        }
    }

    private fun updateStatuses() {
        _waStatuses.value = statusMap.values.flatten()
            .distinctBy { it.uri }
            .sortedByDescending { it.lastModified }
    }
}