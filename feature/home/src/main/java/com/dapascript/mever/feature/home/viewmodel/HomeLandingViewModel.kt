package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
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
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getMeverFolder
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.common.util.getUrlContentType
import com.dapascript.mever.core.common.util.state.ApiState.Error
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.toCurrentDate
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.model.local.VideoGeneralEntity
import com.ketch.Ketch
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.STARTED
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
class HomeLandingViewModel @Inject constructor(
    private val repository: MeverRepository,
    private val ketch: Ketch,
    val connectivityObserver: ConnectivityObserver
) : BaseViewModel() {
    private val meverFolder by lazy { getMeverFolder() }
    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))
    var showBadge by mutableStateOf(false)

    private val _videoState = MutableStateFlow<UiState<List<VideoGeneralEntity>>>(StateInitial)
    val videoState = _videoState.asStateFlow()

    fun getApiDownloader(urlSocialMedia: TextFieldValue) = collectApiAsUiState(
        response = repository.getApiDownloader(urlSocialMedia.text),
        updateState = { _videoState.value = it }
    )

    fun downloadFile(
        url: String,
        platformName: String
    ) {
        if (meverFolder.exists().not()) meverFolder.mkdirs()
        viewModelScope.launch {
            ketch.download(
                url = url,
                path = meverFolder.path,
                fileName = currentTimeMillis().toCurrentDate() + getUrlContentType(url),
                tag = platformName
            )
        }
    }

    fun getObservableKetch() = viewModelScope.launch {
        ketch.observeDownloads().collect { models ->
            showBadge = models.any { it.status in listOf(STARTED, PAUSED, PROGRESS) }
        }
    }

    private fun MeverRepository.getApiDownloader(typeUrl: String) = when (typeUrl.getPlatformType()) {
        FACEBOOK -> getFacebookDownloader(typeUrl)
        INSTAGRAM -> getInstagramDownloader(typeUrl)
        TWITTER -> getTwitterDownloader(typeUrl)
        TIKTOK -> getTikTokDownloader(typeUrl)
        UNKNOWN -> flowOf(Error(Throwable("Unknown platform")))
    }
}