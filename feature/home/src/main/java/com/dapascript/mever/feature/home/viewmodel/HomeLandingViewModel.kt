package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getMeverFolder
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.isAvailableOnLocal
import com.dapascript.mever.core.common.util.state.ApiState.Error
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.toCurrentDate
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.model.local.ContentEntity
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
import kotlinx.coroutines.flow.flowOf
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

    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))
        internal set
    var showBadge by mutableStateOf(false)
        private set
    val showDialogPermission = mutableStateListOf<String>()
    val downloadList = ketch.observeDownloads()
        .map { downloads ->
            downloads
                .filter { it.isAvailableOnLocal() || it.status != SUCCESS }
                .sortedByDescending { it.lastModified }
                .also { showBadge = it.any { file -> file.status in listOf(QUEUED, STARTED, PAUSED, PROGRESS) } }
                .onEach { if (it.status == SUCCESS && it.isAvailableOnLocal().not()) ketch.clearDb(it.id) }
        }
        .stateIn(viewModelScope, Lazily, null)

    private val _contentState = MutableStateFlow<UiState<List<ContentEntity>>>(StateInitial)
    val contentState = _contentState.asStateFlow()

    fun getApiDownloader(urlSocialMedia: TextFieldValue) = collectApiAsUiState(
        response = repository.getApiDownloader(urlSocialMedia.text),
        updateState = { _contentState.value = it }
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
        if (isGranted.not() && showDialogPermission.contains(permission).not()) showDialogPermission.add(permission)
        else onAction()
    }

    private fun MeverRepository.getApiDownloader(typeUrl: String) = when (typeUrl.getPlatformType()) {
        FACEBOOK -> getFacebookDownloader(typeUrl)
        INSTAGRAM -> getInstagramDownloader(typeUrl)
        TWITTER -> getTwitterDownloader(typeUrl)
        TIKTOK -> getTikTokDownloader(typeUrl)
        YOUTUBE -> getYoutubeDownloader(typeUrl)
        UNKNOWN -> flowOf(Error(Throwable("Unknown platform")))
    }
}