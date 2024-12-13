package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.deleteAllMeverFolder
import com.dapascript.mever.core.common.util.getMeverFiles
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryLandingViewModel @Inject constructor(
    private val ketch: Ketch
) : BaseViewModel() {

    private val _downloadList = MutableStateFlow<List<DownloadModel>>(emptyList())
    val downloadList = _downloadList.asStateFlow()

    private val _selectedFilter = MutableStateFlow(UNKNOWN)
    val selectedFilter = _selectedFilter.asStateFlow()

    fun deleteDownload(id: Int) = viewModelScope.launch {
        ketch.clearDb(id)
        _downloadList.value = _downloadList.value.filter { it.id != id }
    }

    fun deleteAllDownloads() = viewModelScope.launch {
        ketch.clearAllDb()
        _downloadList.value = emptyList()
    }

    fun getAllDownloads() = viewModelScope.launch {
        val meverFiles = getMeverFiles()?.map { it.name } ?: emptyList()
        ketch.observeDownloads().collect { downloads ->
            if (downloads.isEmpty()) deleteAllMeverFolder()
            _downloadList.value = downloads.filter { it.isAvailableOnLocal(meverFiles) }
        }
    }

    fun setSelectedFilter(platformType: PlatformType) {
        _selectedFilter.value = platformType
    }

    private fun DownloadModel.isAvailableOnLocal(listPath: List<String>) =
        status == SUCCESS && listPath.contains(fileName)
}