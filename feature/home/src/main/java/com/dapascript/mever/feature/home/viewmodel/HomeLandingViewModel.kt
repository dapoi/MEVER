package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getMeverFolder
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.isAvailableOnLocal
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_SELECTED_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_CONTENTS
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.data.util.GsonHelper.fromJson
import com.dapascript.mever.core.data.worker.DownloaderWorker
import com.ketch.Ketch
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeLandingViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
    dataStore: MeverDataStore,
    private val ketch: Ketch,
    private val workManager: WorkManager
) : BaseViewModel() {
    private val meverFolder by lazy { getMeverFolder() }

    /**
     * Downloader
     */
    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))
    var selectedQuality by mutableStateOf("")
    var showBadge by mutableStateOf(false)
    var showDonationDialog by mutableStateOf(true)
    var contents by mutableStateOf<List<ContentEntity>>(emptyList())

    /**
     * Image Generator
     */
    var promptState by mutableStateOf(TextFieldValue(""))
    var selectedImageCount by mutableIntStateOf(1)
    var selectedArtStyle by mutableStateOf(Pair("", ""))

    val downloadList = ketch.observeDownloads()
        .map { downloads ->
            downloads
                .sortedByDescending { it.timeQueued }
                .also {
                    showBadge = it.any { file ->
                        file.status in listOf(QUEUED, STARTED, PAUSED, PROGRESS)
                    }
                }
                .onEach {
                    if (it.status == SUCCESS && isAvailableOnLocal(it.fileName).not()) {
                        ketch.clearDb(it.id)
                    }
                }
        }
        .stateIn(viewModelScope, Lazily, null)
    val isNetworkAvailable = connectivityObserver
        .observe()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = connectivityObserver.isConnected()
        )
    val isImageGeneratorFeatureActive = dataStore.isImageAiEnabled
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = true
        )
    val youtubeResolutions = dataStore.getYoutubeResolutions
        .map { it.ifEmpty { listOf("360p", "480p", "720p", "1080p") } }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _downloaderResponseState =
        MutableStateFlow<UiState<List<ContentEntity>>>(StateInitial)
    val downloaderResponseState = _downloaderResponseState.asStateFlow()

    fun getApiDownloader() = collectApiAsUiStateWithWorker(
        workManager = workManager,
        workerClass = DownloaderWorker::class.java,
        inputData = workDataOf(
            KEY_REQUEST_URL to urlSocialMediaState.text,
            KEY_REQUEST_SELECTED_QUALITY to selectedQuality
        ),
        onLoading = { _downloaderResponseState.value = StateLoading },
        onSuccess = {
            val data = it.getString(KEY_RESPONSE_CONTENTS).orEmpty()
            val response = data.fromJson<List<ContentEntity>>()
            _downloaderResponseState.value = StateSuccess(response)
            contents = response
        },
        onFailed = { _downloaderResponseState.value = StateFailed(it) }
    )

    fun startDownload(
        url: String,
        fileName: String,
        thumbnail: String
    ) = ketch.download(
        url = url,
        path = meverFolder.path,
        fileName = fileName,
        tag = getPlatformType(urlSocialMediaState.text).platformName,
        metaData = thumbnail
    )

    fun resumeDownload(id: Int) = ketch.resume(id)

    fun pauseDownload(id: Int) = ketch.pause(id)

    fun retryDownload(id: Int) = ketch.retry(id)

    fun delete(id: Int) = ketch.clearDb(id)
}