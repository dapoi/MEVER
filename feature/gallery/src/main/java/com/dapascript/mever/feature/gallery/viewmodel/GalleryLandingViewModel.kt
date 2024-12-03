package com.dapascript.mever.feature.gallery.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
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

    fun getAllDownloads() = viewModelScope.launch {
        ketch.observeDownloads().collect { downloads ->
            _downloadList.value = downloads.filter {
                getMeverFiles()?.map { file -> file.name }?.contains(it.fileName) == true && it.status == SUCCESS
            }
        }
    }

    fun deleteDownload(id: Int) = viewModelScope.launch {
        ketch.clearDb(id)
        _downloadList.value = _downloadList.value.filter { it.id != id }
    }
}