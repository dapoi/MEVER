package com.dapascript.mever.feature.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getMeverFolder
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeImageGeneratorResultRoute
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeImageGeneratorResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    connectivityObserver: ConnectivityObserver,
    private val repository: MeverRepository,
    private val ketch: Ketch
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
            query = "${args.prompt}. Style of images are ${args.artStyle}"
        ),
        resetState = false,
        updateState = { _aiResponseState.value = it }
    )

    fun startDownload(
        url: String,
        fileName: String
    ) = ketch.download(
        url = url,
        fileName = fileName,
        path = meverFolder.path
    )
}
