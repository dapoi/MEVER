package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.storage.StorageUtil.getFilePath
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFiles
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.ketch.Ketch
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeLandingViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
    private val dataStore: MeverDataStore,
    private val ketch: Ketch,
    private val repository: MeverRepository
) : BaseViewModel() {
    private val meverFolder by lazy { getMeverFolder() }

    /**
     * Downloader
     */
    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))
    var selectedQuality by mutableStateOf("")
    var showDonationDialog by mutableStateOf(true)
    var contents by mutableStateOf<List<ContentEntity>>(emptyList())

    /**
     * Image Generator
     */
    var promptState by mutableStateOf(TextFieldValue(""))
    var selectedImageCount by mutableIntStateOf(1)
    var selectedArtStyle by mutableStateOf(Pair("", ""))

    val downloadList = ketch.observeDownloads()
        .distinctUntilChanged()
        .map { downloads ->
            val sortedList = downloads.sortedByDescending { it.timeQueued }
            sortedList.map { downloadModel ->
                downloadModel.copy(path = getFilePath(downloadModel.fileName))
            }
        }
        .distinctUntilChanged()
        .conflate()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    val showBadge = downloadList
        .map { list ->
            list?.any { file ->
                file.status in listOf(QUEUED, STARTED, PAUSED, PROGRESS)
            } ?: false
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = false
        )

    val isNetworkAvailable = connectivityObserver
        .observe()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = connectivityObserver.isConnected()
        )

    val isImageGeneratorFeatureActive = dataStore.isImageAiEnabled.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    val youtubeResolutions = dataStore.getYoutubeVideoAndAudioQuality
        .map { it.ifEmpty { listOf("360p", "480p", "720p", "1080p") } }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = emptyList()
        )

    val getButtonClickCount = dataStore.clickCount.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 1
    )

    val getUrlIntent = dataStore.getUrlIntent.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = ""
    )

    val themeType = dataStore.getTheme.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = System
    )

    private val _downloaderResponseState =
        MutableStateFlow<UiState<List<ContentEntity>>>(StateInitial)
    val downloaderResponseState = _downloaderResponseState.asStateFlow()

    fun getApiDownloader() = collectApiAsUiState(
        response = repository.getDownloader(
            url = urlSocialMediaState.text,
            quality = selectedQuality
        ),
        onLoading = { _downloaderResponseState.value = StateLoading },
        onSuccess = { response ->
            _downloaderResponseState.value = StateSuccess(response)
            contents = response
        },
        onFailed = { _downloaderResponseState.value = StateFailed(it) },
        onReset = { _downloaderResponseState.value = StateInitial }
    )

    fun startDownload(
        url: String,
        fileName: String,
        thumbnail: String
    ) = ketch.download(
        url = url,
        path = meverFolder.path,
        fileName = fileName,
        tag = if (selectedQuality.contains("kbps")) YOUTUBE_MUSIC.platformName
        else getPlatformType(urlSocialMediaState.text).platformName,
        metaData = thumbnail
    )

    fun resumeDownload(id: Int) = ketch.resume(id)

    fun pauseDownload(id: Int) = ketch.pause(id)

    fun retryDownload(id: Int) = ketch.retry(id)

    fun delete(id: Int) = ketch.clearDb(id)

    fun refreshDatabase() {
        viewModelScope.launch {
            val existingNames = getMeverFiles()
                ?.map { it.name.lowercase() }
                ?.toSet()
                ?: emptySet()

            downloadList.value?.forEach { download ->
                if (download.status == SUCCESS) {
                    val exists = existingNames.contains(download.fileName.lowercase())
                    if (exists.not()) ketch.clearDb(download.id)
                }
            }
        }
    }

    fun incrementClickCount() = viewModelScope.launch {
        dataStore.incrementClickCount()
    }

    fun resetUrlIntent() = viewModelScope.launch {
        dataStore.saveUrlIntent("")
    }
}