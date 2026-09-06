package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.storage.StorageUtil.StorageInfo
import com.dapascript.mever.core.data.deeplink.DeeplinkManager
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_IS_GO_IMG_ENABLED
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_IS_IMAGE_AI_ENABLED
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_LINK_CONTENT
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_RESOLUTIONS
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeLandingViewModel @Inject constructor(
    private val repository: MeverRepository,
    private val deeplinkManager: DeeplinkManager
) : BaseViewModel() {

    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))
    var selectedQuality by mutableStateOf("")
    var shouldShowDonationOfferDialog by mutableStateOf(true)
    var contents by mutableStateOf<List<ContentEntity>>(emptyList())
    var errorMessage by mutableStateOf("")
    var storageInfo by mutableStateOf<StorageInfo?>(null)
    var hasCheckedUpdate by mutableStateOf(false)

    private val _refreshTrigger = MutableStateFlow(0)

    val downloadList = repository.observeDownloads()
        .combine(_refreshTrigger) { downloads, _ ->
            downloads
        }
        .distinctUntilChanged()
        .flowOn(Default)
        .stateIn(viewModelScope, WhileSubscribed(5000), null)

    val isAnyDownloadActive = downloadList
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

    val youtubeResolutions = repository.getPreference(KEY_RESOLUTIONS, "").map {
        it.split(",").filter { res -> res.isNotEmpty() }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = emptyList()
    )

    val getButtonClickCount = repository.getClickCount().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 0
    )

    val adsThreshold = repository.getAdsThreshold().stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 3
    )

    val linkContent = repository.getPreference(KEY_LINK_CONTENT, "").stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = ""
    )

    val showSupportedPlatform = repository.getPreference(
        MeverDataStore.KEY_SHOW_SUPPORTED_PLATFORM,
        true
    ).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    val isImageAiEnabled = repository.getPreference(KEY_IS_IMAGE_AI_ENABLED, true).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    val isGoImgEnabled = repository.getPreference(KEY_IS_GO_IMG_ENABLED, true).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = true
    )

    val deeplinkEvent = deeplinkManager.deeplinkEvent.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = null
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
        onFailed = {
            _downloaderResponseState.value = StateFailed(it)
            errorMessage = it
        },
        onReset = { _downloaderResponseState.value = StateInitial }
    )

    fun startDownload(
        url: String,
        fileName: String,
        thumbnail: String
    ) {
        val currentQuality = selectedQuality
        val platformTag = if (currentQuality.contains("kbps")) {
            YOUTUBE_MUSIC.platformName
        } else {
            getPlatformType(urlSocialMediaState.text).platformName
        }

        repository.download(
            url = url,
            fileName = fileName,
            tag = platformTag,
            thumbnail = thumbnail
        )

        if (currentQuality.contains("kbps")) selectedQuality = ""
    }

    fun resumeDownload(id: Int) = repository.resumeDownload(id)

    fun pauseDownload(id: Int) = repository.pauseDownload(id)

    fun retryDownload(id: Int) = repository.retryDownload(id)

    fun delete(id: Int) {
        repository.deleteDownload(id)
        _refreshTrigger.update { it + 1 }
    }

    fun refreshDatabase() {
        _refreshTrigger.update { it + 1 }
        viewModelScope.launch { repository.refreshDownloadDatabase() }
    }

    fun incrementClickCount() = viewModelScope.launch {
        repository.incrementClickCount()
    }

    fun consumeDeeplinkEvent() = deeplinkManager.clearDeeplink()

    fun clearValue(key: String) = viewModelScope.launch {
        repository.clearPreference(key)
    }
}