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
import com.dapascript.mever.core.model.local.ContentEntity
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.PAUSED
import com.ketch.Status.PROGRESS
import com.ketch.Status.STARTED
import com.ketch.Status.SUCCESS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
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
    val tabItems by lazy { listOf("Video", "Image") }
    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))
    var showBadge by mutableStateOf(false)
    var downloadList by mutableStateOf<List<DownloadModel>>(emptyList())
        private set
    var contentState by mutableStateOf<UiState<List<ContentEntity>>>(StateInitial)
        internal set

    fun getApiDownloader(urlSocialMedia: TextFieldValue) = collectApiAsUiState(
        response = repository.getApiDownloader(urlSocialMedia.text),
        updateState = { contentState = it }
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
        ketch.observeDownloads().collect { downloads ->
            showBadge = downloads.any { it.status in listOf(STARTED, PAUSED, PROGRESS) }
            downloadList = downloads.filter { it.status == SUCCESS && it.isAvailableOnLocal() }
        }
    }

    private fun MeverRepository.getApiDownloader(typeUrl: String) = when (typeUrl.getPlatformType()) {
        FACEBOOK -> getFacebookDownloader(typeUrl)
        INSTAGRAM -> getInstagramDownloader(typeUrl)
        TWITTER -> getTwitterDownloader(typeUrl)
        TIKTOK -> getTikTokDownloader(typeUrl)
        YOUTUBE -> getTikTokDownloader(typeUrl)
        /** TODO it's temporary */
        UNKNOWN -> flowOf(Error(Throwable("Unknown platform")))
    }
}