package com.dapascript.mever.feature.gallery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.isAvailableOnLocal
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryLandingViewModel @Inject constructor(
    val ketch: Ketch
) : BaseViewModel() {

    var titleHeight by mutableIntStateOf(0)
        internal set
    var selectedFilter by mutableStateOf(UNKNOWN)
        internal set
    var platformTypes by mutableStateOf(listOf(UNKNOWN))
        internal set
    var downloadList by mutableStateOf<List<DownloadModel>?>(null)
        private set

    fun getAllDownloads() = viewModelScope.launch {
        ketch.observeDownloads().collect { downloads ->
            downloadList = downloads
                .sortedByDescending { it.lastModified }
                .also { platformTypes = PlatformType.entries.filter { type -> it.any { it.tag == type.platformName } } }
                .onEach { if (it.status == SUCCESS && it.isAvailableOnLocal().not()) ketch.clearDb(it.id) }
        }
    }
}