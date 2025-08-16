package com.dapascript.mever.feature.gallery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFiles
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryLandingViewModel @Inject constructor(
    val ketch: Ketch
) : BaseViewModel() {

    var selectedFilter by mutableStateOf(ALL)

    @OptIn(FlowPreview::class)
    val downloadList = ketch.observeDownloads()
        .distinctUntilChanged()
        .map { downloads -> downloads.sortedByDescending { it.timeQueued } }
        .conflate()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)
    val platformTypes = downloadList
        .map { list ->
            PlatformType.entries.filter { type ->
                list?.any { model -> model.tag == type.platformName } ?: false
            }
        }
        .stateIn(viewModelScope, Lazily, listOf(ALL))

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

    fun refreshDatabase() {
        viewModelScope.launch(IO) {
            val existingNames = getMeverFiles()
                ?.map { it.name.lowercase() }
                ?.toSet()
                ?: emptySet()

            downloadList.value?.forEach { download ->
                if (download.status == SUCCESS) {
                    val exists = existingNames.contains(download.fileName.lowercase())
                    if (exists.not()) ketch.clearDb(download.id)
                }
            }
        }
    }
}