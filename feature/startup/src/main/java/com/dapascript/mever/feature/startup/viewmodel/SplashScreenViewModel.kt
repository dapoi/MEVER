package com.dapascript.mever.feature.startup.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.data.BuildConfig.DEBUG
import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    connectivityObserver: ConnectivityObserver,
    private val dataStore: MeverDataStore,
    private val meverRepository: MeverRepository
) : BaseViewModel() {
    val isNetworkAvailable = connectivityObserver
        .observe()
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(),
            initialValue = connectivityObserver.isConnected()
        )
    val isOnboarded = dataStore.isOnboarded.map { it }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = false
    )

    private val _appConfigState = MutableStateFlow<UiState<AppConfigEntity>>(StateInitial)
    val appConfigState = _appConfigState.asStateFlow()

    fun getAppConfig() {
        if (DEBUG) {
            _appConfigState.value = StateLoading
            viewModelScope.launch {
                delay(500)
                val mockAppConfig = AppConfigEntity(
                    version = "1.0.0",
                    isImageGeneratorFeatureActive = true,
                    youtubeResolutions = listOf("360p", "480p", "720p")
                )
                _appConfigState.value = StateSuccess(mockAppConfig)
                with(dataStore) {
                    saveVersion(mockAppConfig.version)
                    setIsImageAiEnabled(mockAppConfig.isImageGeneratorFeatureActive)
                    saveYoutubeResolutions(mockAppConfig.youtubeResolutions)
                }
            }
        } else {
            collectApiAsUiState(
                response = meverRepository.getAppConfig(),
                onLoading = { _appConfigState.value = StateLoading },
                onSuccess = {
                    _appConfigState.value = StateSuccess(it)
                    viewModelScope.launch {
                        with(dataStore) {
                            saveVersion(it.version)
                            setIsImageAiEnabled(it.isImageGeneratorFeatureActive)
                            saveYoutubeResolutions(it.youtubeResolutions)
                        }
                    }
                },
                onFailed = { _appConfigState.value = StateFailed(it) }
            )
        }
    }
}