package com.dapascript.mever.feature.gallery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.getMeverFiles
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryLandingViewModel @Inject constructor(
    private val ketch: Ketch
) : BaseViewModel() {

    var downloadList by mutableStateOf(listOf<DownloadModel>())
        internal set

    var selectedFilter by mutableStateOf(UNKNOWN)
        internal set

    fun deleteDownload(id: Int) = viewModelScope.launch {
        ketch.clearDb(id)
    }

    fun deleteAllDownloads() = viewModelScope.launch {
        ketch.clearAllDb()
    }

    fun getAllDownloads() = viewModelScope.launch {
        ketch.observeDownloads().collect { downloads ->
            downloadList = downloads.filter { it.isAvailableOnLocal() }
        }
    }

    private fun DownloadModel.isAvailableOnLocal() =
        status == SUCCESS && getMeverFiles()?.map { it.name }.orEmpty().contains(fileName)
}