package com.dapascript.mever.feature.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeImageGeneratorResultRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeImageGeneratorResultViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: MeverRepository,
    val connectivityObserver: ConnectivityObserver
) : BaseViewModel() {
    val args by lazy { savedStateHandle.toRoute<HomeImageGeneratorResultRoute>() }

    private val _aiResponseState = MutableStateFlow<UiState<ImageAiEntity>>(StateInitial)
    val aiResponseState = _aiResponseState.asStateFlow()

    fun getImageAiGenerator() = collectApiAsUiState(
        response = repository.getImageAiGenerator(
            query = "${args.prompt}. Style of images are ${args.artStyle}"
        ),
        updateState = { _aiResponseState.value = it }
    )
}