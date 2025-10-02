package com.dapascript.mever.feature.gallery.viewmodel

import android.content.Context
import android.media.MediaScannerConnection
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.storage.StorageUtil.getFilePath
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFiles
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val ketch: Ketch
) : BaseViewModel() {

    private val meverFolder by lazy { getMeverFolder() }

    var selectedFilter by mutableStateOf(ALL)
    var titleHeight by mutableIntStateOf(0)

    @OptIn(FlowPreview::class)
    val downloadList = ketch.observeDownloads()
        .map { downloads ->
            downloads.map {
                it.copy(
                    path = getFilePath(
                        dir = meverFolder,
                        fileName = it.fileName
                    )?.absolutePath.orEmpty()
                )
            }
        }
        .distinctUntilChanged()
        .conflate()
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

    fun resumeDownload(id: Int) = ketch.resume(id)

    fun pauseDownload(id: Int) = ketch.pause(id)

    fun pauseAllDownloads() = ketch.pauseAll()

    fun retryDownload(id: Int) = ketch.retry(id)

    fun delete(id: Int) = ketch.clearDb(id)

    fun deleteAll() = ketch.clearAllDb()

    fun syncToGallery(context: Context, fileName: String) {
        viewModelScope.launch {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(getFilePath(meverFolder, fileName)?.absolutePath),
                null,
                null
            )
        }
    }

    fun refreshDatabase() {
        viewModelScope.launch {
            val existingNames = getMeverFiles(meverFolder)
                ?.map { it.name.lowercase() }
                ?.toSet()
                ?: emptySet()
            val downloads = downloadList.value ?: return@launch

            downloads
                .filter {
                    it.status == SUCCESS && existingNames.contains(it.fileName.lowercase()).not()
                }
                .forEach { ketch.clearDb(it.id) }
        }
    }
}