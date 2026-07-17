package com.dapascript.mever.feature.ai.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.AiBackgroundRemovalProcessor
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.saveBitmapToFile
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
class AiBackgroundRemovalViewModel @Inject constructor(
    private val processor: AiBackgroundRemovalProcessor,
    private val dataStore: MeverDataStore,
    private val repository: MeverRepository,
    private val ketch: Ketch
) : BaseViewModel() {

    private val meverFolder by lazy { getMeverFolder() }

    val getButtonClickCount = dataStore.clickCount.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 1
    )

    private val _backgroundRemovalState = MutableStateFlow<UiState<Bitmap>>(StateInitial)
    val backgroundRemovalState = _backgroundRemovalState.asStateFlow()

    private val _saveImageState = MutableStateFlow<UiState<Boolean>>(StateInitial)
    val saveImageState = _saveImageState.asStateFlow()

    fun removeBackground(context: Context, imageUri: Uri) {
        _backgroundRemovalState.value = StateLoading
        viewModelScope.launch {
            val result = processor.removeBackground(context.contentResolver, imageUri)
            _backgroundRemovalState.value = if (result != null) {
                StateSuccess(result)
            } else {
                StateFailed(context.getString(R.string.failed_process_ai))
            }
        }
    }

    fun saveImage(context: Context, bitmap: Bitmap) {
        val timeStamp = changeToCurrentDate(currentTimeMillis())
        val fileName = "MEVER_BG_REMOVAL_$timeStamp.png"

        collectApiAsUiState(
            response = repository.uploadImage(bitmap, fileName),
            onLoading = { _saveImageState.value = StateLoading },
            onSuccess = { url ->
                if (url != null) {
                    ketch.download(
                        url = url,
                        path = meverFolder.absolutePath,
                        fileName = fileName,
                        tag = AI.platformName,
                        metaData = url
                    )
                    _saveImageState.value = StateSuccess(true)
                } else {
                    viewModelScope.launch { saveImageLocally(context, bitmap, fileName) }
                }
            },
            onFailed = { viewModelScope.launch { saveImageLocally(context, bitmap, fileName) } }
        )
    }

    fun reset() {
        _backgroundRemovalState.value = StateInitial
        _saveImageState.value = StateInitial
    }

    fun incrementClickCount() = viewModelScope.launch {
        dataStore.incrementClickCount()
    }

    private suspend fun saveImageLocally(context: Context, bitmap: Bitmap, fileName: String) {
        val destFile = File(meverFolder, fileName)
        val isSuccess = withContext(IO) {
            saveBitmapToFile(bitmap, destFile, true)
        }

        if (isSuccess) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(destFile.absolutePath),
                arrayOf("image/png"),
                null
            )
            _saveImageState.value = StateSuccess(false)
        } else {
            _saveImageState.value = StateFailed(context.getString(R.string.failed_save_image))
        }
    }
}