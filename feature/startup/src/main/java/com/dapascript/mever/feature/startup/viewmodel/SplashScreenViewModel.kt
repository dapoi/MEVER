package com.dapascript.mever.feature.startup.viewmodel

import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.data.BuildConfig.DEBUG
import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_IS_GO_IMG_ENABLED
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_IS_IMAGE_AI_ENABLED
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_IS_ONBOARDED
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_RESOLUTIONS
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_SHOW_SUPPORTED_PLATFORM
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_VERSION
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class SplashScreenViewModel @Inject constructor(
    private val meverRepository: MeverRepository
) : BaseViewModel() {
    val isOnboarded = meverRepository.getPreference(KEY_IS_ONBOARDED, false).stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = null
    )

    val getAppVersion = meverRepository.getPreference(KEY_VERSION, "1.0.0").stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = "1.0.0"
    )

    private val _appConfigState = MutableStateFlow<UiState<AppConfigEntity>>(StateInitial)
    val appConfigState = _appConfigState.asStateFlow()

    fun getAppConfig() {
        if (DEBUG) {
            _appConfigState.value = StateLoading
            viewModelScope.launch {
                delay(1.seconds)
                val mockAppConfig = AppConfigEntity(
                    isImageGeneratorFeatureActive = true,
                    isGoImgFeatureActive = true,
                    showSupportedPlatform = true,
                    videoResolutionsAndAudioQualities = mapOf(
                        "video" to listOf("360p", "480p", "720p"),
                        "audio" to listOf("128kbps")
                    ),
                    maintenanceDay = null
                )
                _appConfigState.value = StateSuccess(mockAppConfig)
                with(meverRepository) {
                    savePreference(
                        KEY_IS_IMAGE_AI_ENABLED,
                        mockAppConfig.isImageGeneratorFeatureActive
                    )
                    savePreference(
                        KEY_IS_GO_IMG_ENABLED,
                        mockAppConfig.isGoImgFeatureActive
                    )
                    savePreference(
                        KEY_SHOW_SUPPORTED_PLATFORM,
                        mockAppConfig.showSupportedPlatform
                    )
                    savePreference(
                        KEY_RESOLUTIONS,
                        if (mockAppConfig.videoResolutionsAndAudioQualities.isNotEmpty()) {
                            mockAppConfig.videoResolutionsAndAudioQualities.values.flatten()
                                .joinToString(",")
                        } else ""
                    )
                }
            }
        } else {
            collectApiAsUiState(
                response = meverRepository.getAppConfig(),
                onLoading = { _appConfigState.value = StateLoading },
                onSuccess = { response ->
                    _appConfigState.value = StateSuccess(response)
                    response?.let {
                        viewModelScope.launch {
                            with(meverRepository) {
                                savePreference(
                                    KEY_IS_IMAGE_AI_ENABLED,
                                    it.isImageGeneratorFeatureActive
                                )
                                savePreference(
                                    KEY_IS_GO_IMG_ENABLED,
                                    it.isGoImgFeatureActive
                                )
                                savePreference(
                                    KEY_SHOW_SUPPORTED_PLATFORM,
                                    it.showSupportedPlatform
                                )
                                savePreference(
                                    KEY_RESOLUTIONS,
                                    if (it.videoResolutionsAndAudioQualities.isNotEmpty()) {
                                        it.videoResolutionsAndAudioQualities.values.flatten()
                                            .joinToString(",")
                                    } else ""
                                )
                            }
                        }
                    }
                },
                onFailed = { _appConfigState.value = StateFailed(it) },
                onReset = { _appConfigState.value = StateInitial }
            )
        }
    }
}