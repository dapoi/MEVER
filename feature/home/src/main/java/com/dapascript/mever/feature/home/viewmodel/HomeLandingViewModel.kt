package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getMeverFolder
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.isAvailableOnLocal
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.toCurrentDate
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.ketch.Ketch
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
class HomeLandingViewModel @Inject constructor(
    private val repository: MeverRepository,
    val ketch: Ketch,
    val connectivityObserver: ConnectivityObserver
) : BaseViewModel() {
    private val meverFolder by lazy { getMeverFolder() }

    /**
     * Downloader
     */
    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))
        internal set
    var selectedQuality by mutableStateOf("")
        internal set
    var showBadge by mutableStateOf(false)
        private set

    /**
     * Image Generator
     */
    var promptState by mutableStateOf(TextFieldValue(""))
        internal set
    var selectedImageCount by mutableIntStateOf(1)
        internal set
    var selectedArtStyle by mutableStateOf(Pair("", ""))
        internal set

    val showDialogPermission = mutableStateListOf<String>()
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
                    if (it.status == SUCCESS && it.isAvailableOnLocal().not()) {
                        ketch.clearDb(it.id)
                    }
                }
        }
        .stateIn(viewModelScope, Lazily, null)

    private val _downloaderResponseState = MutableStateFlow<UiState<List<ContentEntity>>>(StateInitial)
    val downloaderResponseState = _downloaderResponseState.asStateFlow()

    fun getApiDownloader(urlSocialMedia: TextFieldValue) = collectApiAsUiState(
        response = repository.getApiDownloader(urlSocialMedia.text),
        updateState = { _downloaderResponseState.value = it }
    )

    fun downloadFile(
        url: String,
        platformName: String,
        thumbnail: String
    ) {
        if (meverFolder.exists().not()) meverFolder.mkdirs()
        viewModelScope.launch {
            ketch.download(
                url = url,
                path = meverFolder.path,
                fileName = currentTimeMillis().toCurrentDate() + getUrlContentType(url),
                tag = platformName,
                metaData = thumbnail
            )
        }
    }

    fun dismissDialog() = showDialogPermission.removeAt(0)

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean,
        onAction: () -> Unit = {}
    ) {
        if (isGranted.not() && showDialogPermission.contains(permission)
                .not()
        ) showDialogPermission.add(permission)
        else onAction()
    }

    private fun MeverRepository.getApiDownloader(
        typeUrl: String
    ) = when (typeUrl.getPlatformType()) {
        FACEBOOK -> getFacebookDownloader(typeUrl)
        YOUTUBE -> getYoutubeDownloader(typeUrl, selectedQuality)
        else -> getSavefromDownloader(typeUrl)
    }
}