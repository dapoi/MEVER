package com.dapascript.mever.feature.ai.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.AiBackgroundRemovalProcessor
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.saveBitmapToFile
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.data.source.local.MeverDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.lang.System.currentTimeMillis
import javax.inject.Inject

@HiltViewModel
class AiBackgroundRemovalViewModel @Inject constructor(
    private val processor: AiBackgroundRemovalProcessor,
    private val dataStore: MeverDataStore
) : BaseViewModel() {

    private val meverFolder by lazy { getMeverFolder() }

    val getButtonClickCount = dataStore.clickCount.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 1
    )

    private val _backgroundRemovalState = MutableStateFlow<UiState<Bitmap>>(StateInitial)
    val backgroundRemovalState = _backgroundRemovalState.asStateFlow()

    private val _saveImageState = MutableStateFlow<UiState<Unit>>(StateInitial)
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
        _saveImageState.value = StateLoading
        viewModelScope.launch {
            try {
                val timeStamp = changeToCurrentDate(currentTimeMillis())
                val fileName = "MEVER_BG_REMOVAL_$timeStamp.png"
                val destFile = File(meverFolder, fileName)
                
                val isSuccess = saveBitmapToFile(
                    bitmap = bitmap,
                    file = destFile,
                    isPng = true
                )

                if (isSuccess) {
                    // Sync ke Android Gallery biar muncul di app gallery sistem
                    MediaScannerConnection.scanFile(
                        context, 
                        arrayOf(destFile.absolutePath), 
                        arrayOf("image/png"), 
                        null
                    )
                    _saveImageState.value = StateSuccess(Unit)
                } else {
                    _saveImageState.value = StateFailed(context.getString(R.string.failed_save_image))
                }
            } catch (e: Exception) {
                _saveImageState.value = StateFailed(e.message ?: "Unknown error occurred.")
            }
        }
    }

    fun reset() {
        _backgroundRemovalState.value = StateInitial
        _saveImageState.value = StateInitial
    }

    fun incrementClickCount() = viewModelScope.launch {
        dataStore.incrementClickCount()
    }
}
