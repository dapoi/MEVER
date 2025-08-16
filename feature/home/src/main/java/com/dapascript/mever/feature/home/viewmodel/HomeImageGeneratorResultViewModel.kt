package com.dapascript.mever.feature.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeImageGeneratorResultRoute
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
class HomeImageGeneratorResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    connectivityObserver: ConnectivityObserver,
    private val ketch: Ketch,
    private val repository: MeverRepository
) : BaseViewModel() {
    private val meverFolder by lazy { getMeverFolder() }
    val args by lazy { savedStateHandle.toRoute<HomeImageGeneratorResultRoute>() }
    val isNetworkAvailable = connectivityObserver
        .observe()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = connectivityObserver.isConnected()
        )

    private val _aiResponseState = MutableStateFlow<UiState<ImageAiEntity>>(StateInitial)
    val aiResponseState = _aiResponseState.asStateFlow()

    fun getImageAiGenerator() = collectApiAsUiState(
        response = repository.getImageAiGenerator(
            prompt = "${args.prompt}. Style ${args.artStyle.ifEmpty { "Realistic" }}"
        ),
        onLoading = { _aiResponseState.value = StateLoading },
        onSuccess = { _aiResponseState.value = StateSuccess(it) },
        onFailed = { _aiResponseState.value = StateFailed(it) }
    )

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