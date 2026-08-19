package com.dapascript.mever.feature.gallery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.data.repository.MeverRepository
import com.ketch.DownloadModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GalleryLandingViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {

    var selectedFilter by mutableStateOf(ALL)

    private val _refreshTrigger = MutableStateFlow(0)

    val downloadList = repository.observeDownloads()
        .combine(_refreshTrigger) { downloads, _ ->
            downloads
        }
        .distinctUntilChanged()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    val platformTypes = downloadList
        .map { list ->
            val uniqueTags = list?.map { it.tag }?.toSet() ?: emptySet()
            PlatformType.entries.filter { it.platformName in uniqueTags }
        }
        .distinctUntilChanged()
        .stateIn(viewModelScope, WhileSubscribed(5000), listOf(ALL))

    private val _selectedItems = MutableStateFlow<Set<DownloadModel>>(emptySet())
    val selectedItems = _selectedItems.asStateFlow()

    fun toggleSelection(item: DownloadModel) {
        _selectedItems.update { currentItem ->
            if (item in currentItem) currentItem - item else currentItem + item
        }
    }

    fun toggleSelectionAll(items: List<DownloadModel>) {
        _selectedItems.update { items.toSet() }
    }

    fun clearSelection() {
        _selectedItems.value = emptySet()
    }

    fun resumeDownload(id: Int) = repository.resumeDownload(id)

    fun pauseDownload(id: Int) = repository.pauseDownload(id)

    fun pauseAllDownloads() = repository.pauseAllDownloads()

    fun retryDownload(id: Int) = repository.retryDownload(id)

    fun delete(id: Int) {
        repository.deleteDownload(id)
        _refreshTrigger.update { it + 1 }
    }

    fun deleteItems(ids: List<Int>) {
        repository.deleteDownloads(ids)
        _refreshTrigger.update { it + 1 }
    }

    fun deleteAll() {
        repository.deleteAllDownloads()
        _refreshTrigger.update { it + 1 }
    }

    fun refreshDatabase() {
        _refreshTrigger.update { it + 1 }
        viewModelScope.launch { repository.refreshDownloadDatabase() }
    }
}