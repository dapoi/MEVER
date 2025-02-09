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
import com.ketch.Ketch
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
    val downloadList = ketch.observeDownloads()
        .map { downloads ->
            downloads
                .filter { it.isAvailableOnLocal() || it.status != SUCCESS }
                .sortedByDescending { it.lastModified }
                .also { platformTypes = PlatformType.entries.filter { type -> it.any { it.tag == type.platformName } } }
                .onEach { if (it.status == SUCCESS && it.isAvailableOnLocal().not()) ketch.clearDb(it.id) }
        }
        .stateIn(viewModelScope, Lazily, null)
}