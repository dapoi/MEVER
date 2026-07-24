package com.dapascript.mever.feature.ai.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.feature.ai.BuildConfig.DEBUG
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.System.currentTimeMillis
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AiImageResultViewModel @Inject constructor(
    private val ketch: Ketch,
    private val repository: MeverRepository
) : BaseViewModel() {
    private val meverFolder by lazy { getMeverFolder() }

    private val _aiResponseState = MutableStateFlow<UiState<ImageAiEntity?>>(StateInitial)
    val aiResponseState = _aiResponseState.asStateFlow()

    private val _aiReportState = MutableStateFlow<UiState<Unit>>(StateInitial)
    val aiReportState = _aiReportState.asStateFlow()

    fun getImageAiGenerator(
        prompt: String,
        artStyle: String
    ) = collectApiAsUiState(
        response = repository.getImageAiGenerator(
            prompt = "$prompt. Art style: $artStyle."
        ),
        state = _aiResponseState
    )

    fun postReportAiImage(message: String) {
        if (DEBUG) {
            _aiReportState.value = StateLoading
            viewModelScope.launch {
                delay(1.seconds)
                _aiReportState.value = StateSuccess(Unit)
            }
        } else collectApiAsUiState(
            response = repository.postReportAiImage(message),
            state = _aiReportState
        )
    }

    fun startDownload(url: String) {
        if (url.isBlank()) return
        ketch.download(
            url = url,
            fileName = changeToCurrentDate(currentTimeMillis()) + ".jpg",
            path = meverFolder.path,
            tag = AI.platformName
        )
    }
}