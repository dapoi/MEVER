package com.dapascript.mever.feature.gallery.viewmodel

import android.content.Context
import android.media.MediaScannerConnection
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.EXPLORE
import com.dapascript.mever.core.common.util.storage.StorageUtil.getFilePath
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFiles
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import com.ketch.Status.SUCCESS
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GalleryLandingViewModel @Inject constructor(
    private val ketch: Ketch
) : BaseViewModel() {

    private val meverFolder by lazy { getMeverFolder() }

    var selectedFilter by mutableStateOf(ALL)

    private val _refreshTrigger = MutableStateFlow(0)

    val downloadList = ketch.observeDownloads()
        .combine(_refreshTrigger) { downloads, _ ->
            val folderFiles = getMeverFiles(meverFolder)
            val ketchFiles = downloads.map { it.fileName.lowercase() }.toSet()
            
            val ketchItems = downloads.map {
                it.copy(
                    path = getFilePath(
                        dir = meverFolder,
                        fileName = it.fileName
                    )?.absolutePath.orEmpty()
                )
            }
            
            val localItems = folderFiles
                .filter { it.name.lowercase() !in ketchFiles }
                .map { file ->
                    DownloadModel(
                        id = file.name.hashCode(),
                        url = "file://${file.absolutePath}",
                        path = file.absolutePath,
                        fileName = file.name,
                        tag = if (file.name.contains("BG_REMOVAL", true)) AI.platformName else EXPLORE.platformName,
                        status = SUCCESS,
                        progress = 100,
                        total = file.length(),
                        metaData = file.absolutePath,
                        eTag = "",
                        failureReason = "",
                        headers = hashMapOf(),
                        lastModified = file.lastModified(),
                        speedInBytePerMs = 0f,
                        timeQueued = file.lastModified()
                    )
                }
            
            (ketchItems + localItems).sortedWith(
                compareByDescending<DownloadModel> { 
                    it.status in listOf(QUEUED, STARTED, PROGRESS) 
                }.thenByDescending { it.lastModified }
            )
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

    fun resumeDownload(id: Int) = ketch.resume(id)

    fun pauseDownload(id: Int) = ketch.pause(id)

    fun pauseAllDownloads() = ketch.pauseAll()

    fun retryDownload(id: Int) = ketch.retry(id)

    fun delete(id: Int) {
        val item = downloadList.value?.find { it.id == id }
        if (item != null && item.url.startsWith("file://")) {
            File(item.path).delete()
            _refreshTrigger.update { it + 1 }
        } else {
            ketch.clearDb(id)
        }
    }

    fun deleteAll() {
        ketch.clearAllDb()
        meverFolder.listFiles()?.forEach { it.delete() }
        _refreshTrigger.update { it + 1 }
    }

    fun syncToGallery(context: Context, fileName: String) {
        viewModelScope.launch {
            getFilePath(meverFolder, fileName)?.let { filePath ->
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(filePath.absolutePath),
                    null,
                    null
                )
            }
        }
    }

    fun refreshDatabase() {
        _refreshTrigger.update { it + 1 }
        viewModelScope.launch {
            val existingNames = getMeverFiles(meverFolder)
                .map { it.name.lowercase() }
                .toSet()
            val downloads = downloadList.value ?: return@launch

            downloads
                .filter {
                    it.status == SUCCESS && existingNames.contains(it.fileName.lowercase()).not()
                }
                .forEach { ketch.clearDb(it.id) }
        }
    }
}