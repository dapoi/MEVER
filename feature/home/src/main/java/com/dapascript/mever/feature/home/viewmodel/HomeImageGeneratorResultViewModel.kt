package com.dapascript.mever.feature.home.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver
import com.dapascript.mever.core.common.util.getMeverFolder
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_IMAGES
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_PROMPT
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.work.ImageGeneratorWorker
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
    private val workManager: WorkManager
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

    fun getImageAiGenerator() = collectApiAsUiStateWithWorker(
        workManager = workManager,
        workerClass = ImageGeneratorWorker::class.java,
        updateState = { _aiResponseState.value = it },
        inputData = workDataOf(
            KEY_REQUEST_PROMPT to "${args.prompt}. Style of images are ${args.artStyle}"
        ),
        transformResponses = {
            ImageAiEntity(
                prompt = it.getString(KEY_RESPONSE_PROMPT).orEmpty(),
                imagesUrl = it.getStringArray(KEY_RESPONSE_IMAGES)?.toList() ?: emptyList()
            )
        }
    )

    fun startDownload(url: String) = ketch.download(
        url = url,
        fileName = changeToCurrentDate(currentTimeMillis()) + ".jpg",
        path = meverFolder.path,
        tag = AI.platformName
    )
}
