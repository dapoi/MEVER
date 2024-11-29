package com.dapascript.mever.feature.notification.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.CANCELLED
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.STARTED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val ketch: Ketch
) : BaseViewModel() {

    private val _downloadList = MutableStateFlow<List<DownloadModel>>(emptyList())
    val downloadList = _downloadList.asStateFlow()

    fun getAllDownloads() = viewModelScope.launch {
        ketch.observeDownloads().collect { downloads ->
            _downloadList.value = downloads.filter { it.status in listOf(STARTED,PAUSED, PROGRESS) }
            ketch.clearDb(downloads.find { it.status in listOf(CANCELLED, FAILED) }?.id ?: 0)
        }
    }

    fun stateResumeOrPauseDownload(isPause: Boolean, id: Int) = if (isPause) ketch.resume(id) else ketch.pause(id)
}