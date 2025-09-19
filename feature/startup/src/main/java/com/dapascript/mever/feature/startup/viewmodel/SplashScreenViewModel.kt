package com.dapascript.mever.feature.startup.viewmodel

import android.content.Context
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
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val dataStore: MeverDataStore,
    private val meverRepository: MeverRepository,
    @param:ApplicationContext private val context: Context
) : BaseViewModel() {

    val today by lazy {
        LocalDate.now().dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
    }

    val isOnboarded = dataStore.isOnboarded.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = false
    )

    private val _appConfigState = MutableStateFlow<UiState<AppConfigEntity>>(StateInitial)
    val appConfigState = _appConfigState.asStateFlow()

    init {
        viewModelScope.launch { getAppConfig() }
    }

    fun getAppConfig() {
        if (DEBUG || _appConfigState.value is StateFailed) {
            _appConfigState.value = StateLoading
            viewModelScope.launch {
                delay(500)
                val mockAppConfig = AppConfigEntity(
                    version = context.packageManager.getPackageInfo(
                        context.packageName,
                        0
                    ).versionName.orEmpty(),
                    isImageGeneratorFeatureActive = true,
                    isGoImgFeatureActive = true,
                    videoResolutionsAndAudioQualities = mapOf(
                        "video" to listOf("360p", "480p", "720p"),
                        "audio" to listOf("128kbps")
                    ),
                    maintenanceDay = null
                )
                _appConfigState.value = StateSuccess(mockAppConfig)
                with(dataStore) {
                    saveVersion(mockAppConfig.version)
                    setIsImageAiEnabled(mockAppConfig.isImageGeneratorFeatureActive)
                    setIsGoImgEnabled(mockAppConfig.isGoImgFeatureActive)
                    saveYoutubeVideoAndAudioQuality(mockAppConfig.videoResolutionsAndAudioQualities)
                }
            }
        } else {
            collectApiAsUiState(
                response = meverRepository.getAppConfig(),
                onLoading = { _appConfigState.value = StateLoading },
                onSuccess = {
                    _appConfigState.value = StateSuccess(it)
                    viewModelScope.launch {
                        it?.let {
                            with(dataStore) {
                                saveVersion(it.version)
                                setIsImageAiEnabled(it.isImageGeneratorFeatureActive)
                                setIsGoImgEnabled(it.isGoImgFeatureActive)
                                saveYoutubeVideoAndAudioQuality(it.videoResolutionsAndAudioQualities)
                            }
                        }
                    }
                },
                onFailed = { _appConfigState.value = StateFailed(it) }
            )
        }
    }
}