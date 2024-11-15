package com.dapascript.mever.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.model.local.VideoUrlEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MeverRepository
) : BaseViewModel() {

    private val _urlState = MutableStateFlow<UiState<List<VideoUrlEntity>>>(StateInitial)
    val urlState = _urlState.asStateFlow()

    var domain by mutableStateOf(TextFieldValue(""))

    fun getVideoDownloader() = collectApiAsUiState(
        flow = repository.getVideoDownloader(domain.text),
        updateState = { _urlState.value = it }
    )
}