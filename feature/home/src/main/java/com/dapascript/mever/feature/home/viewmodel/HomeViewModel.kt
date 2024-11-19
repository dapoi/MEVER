package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.model.local.VideoGeneralEntity
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MeverRepository,
    ketch: Ketch
) : BaseViewModel() {

    val ketch by lazy { ketch }
    var urlSocialMediaState by mutableStateOf(TextFieldValue(""))

    private val _videoState = MutableStateFlow<UiState<List<VideoGeneralEntity>>>(StateInitial)
    val videoState = _videoState.asStateFlow()

    fun getApiDownloader(urlSocialMedia: TextFieldValue) = collectApiAsUiState(
        response = repository.getApiDownloader(urlSocialMedia.text),
        updateState = { _videoState.value = it }
    )

    fun setUrlSocialMedia(url: TextFieldValue) {
        urlSocialMediaState = url
    }

    fun resetState() {
        _videoState.value = StateInitial
    }

    private fun MeverRepository.getApiDownloader(typeUrl: String) = when {
        typeUrl.contains("facebook") -> getFacebookDownloader(typeUrl)
        typeUrl.contains("instagram") -> getInstagramDownloader(typeUrl)
        typeUrl.contains("twitter") -> getTwitterDownloader(typeUrl)
        else -> throw IllegalArgumentException("Invalid type url")
    }
}