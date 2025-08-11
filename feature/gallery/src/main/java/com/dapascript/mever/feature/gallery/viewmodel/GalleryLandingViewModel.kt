package com.dapascript.mever.feature.gallery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.storage.StorageUtil.isAvailableOnLocal
import com.ketch.Ketch
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryLandingViewModel @Inject constructor(
    val ketch: Ketch
) : BaseViewModel() {

    var selectedFilter by mutableStateOf(ALL)

    @OptIn(FlowPreview::class)
    val downloadList = ketch.observeDownloads()
        .map { downloads ->
               downloads.sortedByDescending { it.timeQueued }
        }
        .distinctUntilChanged()
        .sample(16)
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)
    val platformTypes = downloadList
        .map { list ->
            PlatformType.entries.filter { type ->
                list?.any { model -> model.tag == type.platformName } ?: false
            }
        }
        .stateIn(viewModelScope, Lazily, listOf(ALL))

    fun refreshDatabase() {
        viewModelScope.launch(IO) {
            val currentDownloads = downloadList.value
            currentDownloads?.forEach { download ->
                if (download.status == SUCCESS && isAvailableOnLocal(download.fileName).not()) {
                    ketch.clearDb(download.id)
                }
            }
        }
    }
}