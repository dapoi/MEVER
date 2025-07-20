package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.Constant.PlatformType.AI
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getMeverFolder
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.isAvailableOnLocal
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
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
    private val repository: MeverRepository,
    private val ketch: Ketch
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
                    it.all { file -> file.status == SUCCESS }.let { valid ->
                        if (valid) urlSocialMediaState = TextFieldValue("")
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

    private val _downloaderResponseState =
        MutableStateFlow<UiState<List<ContentEntity>>>(StateInitial)
    val downloaderResponseState = _downloaderResponseState.asStateFlow()

    fun getApiDownloader() = collectApiAsUiState(
        response = repository.getApiDownloader(urlSocialMediaState.text),
        onLoading = { _downloaderResponseState.value = StateLoading },
        onSuccess = {
            _downloaderResponseState.value = StateSuccess(it)
            contents = it
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
        tag = getPlatformType(urlSocialMediaState.text).platformName,
        metaData = thumbnail
    )

    fun resumeDownload(id: Int) = ketch.resume(id)

    fun pauseDownload(id: Int) = ketch.pause(id)

    fun retryDownload(id: Int) = ketch.retry(id)

    fun delete(id: Int) = ketch.clearDb(id)

    private fun MeverRepository.getApiDownloader(url: String) = when (getPlatformType(url)) {
        FACEBOOK -> getFacebookDownloader(url)
        INSTAGRAM -> getInstagramDownloader(url)
        TIKTOK -> getTiktokDownloader(url)
        TWITTER -> getTwitterDownloader(url)
        YOUTUBE -> getYoutubeDownloader(url, selectedQuality)
        AI, UNKNOWN -> {
            _downloaderResponseState.value = StateFailed(
                Throwable("Platform not supported")
            )
            throw Throwable("Platform not supported")
        }
    }
}