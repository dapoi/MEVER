package com.dapascript.mever.feature.notification.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.CANCELLED
import com.ketch.Status.FAILED
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationLandingViewModel @Inject constructor(
    private val ketch: Ketch
) : BaseViewModel() {

    var downloadList by mutableStateOf(listOf<DownloadModel>())
        internal set

    var snackbarMessage by mutableStateOf("")

    fun getAllDownloads() = viewModelScope.launch {
        var previouslyIncomplete = false

        ketch.observeDownloads().collect { downloads ->
            // Filter out the downloads that are not in progress
            downloadList = downloads.filter { it.status in listOf(QUEUED, STARTED, PAUSED, PROGRESS) }

            // Check completion
            if (downloadList.isNotEmpty()) {
                val allComplete = downloadList.all { it.progress == 100 }
                if (allComplete && previouslyIncomplete) snackbarMessage = "All downloads are completed!"
                previouslyIncomplete = allComplete.not()
            }

            // Clear the database of the first download that is either cancelled or failed
            ketch.clearDb(downloads.find { it.status in listOf(CANCELLED, FAILED) }?.id ?: 0)
        }
    }

    fun stateResumeOrPauseDownload(isPause: Boolean, id: Int) = if (isPause) ketch.resume(id) else ketch.pause(id)
}