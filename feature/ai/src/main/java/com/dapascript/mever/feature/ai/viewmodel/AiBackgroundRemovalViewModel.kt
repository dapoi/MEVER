package com.dapascript.mever.feature.ai.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.core.common.util.BackgroundRemovalProcessor
import com.dapascript.mever.core.common.util.PlatformType.AI
import com.dapascript.mever.core.common.util.addBackground
import com.dapascript.mever.core.common.util.changeToCurrentDate
import com.dapascript.mever.core.common.util.decodeResizedBitmap
import com.dapascript.mever.core.common.util.saveBitmapToFile
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.common.util.state.UiState.StateFailed
import com.dapascript.mever.core.common.util.state.UiState.StateInitial
import com.dapascript.mever.core.common.util.state.UiState.StateLoading
import com.dapascript.mever.core.common.util.state.UiState.StateSuccess
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.feature.ai.viewmodel.AiBackgroundRemovalViewModel.ImageLocation.GALLERY
import com.dapascript.mever.feature.ai.viewmodel.AiBackgroundRemovalViewModel.ImageLocation.IN_APP
import com.ketch.Ketch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.timeout
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.System.currentTimeMillis
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AiBackgroundRemovalViewModel @Inject constructor(
    private val processor: BackgroundRemovalProcessor,
    private val dataStore: MeverDataStore,
    private val repository: MeverRepository,
    private val ketch: Ketch
) : BaseViewModel() {

    private val meverFolder by lazy { getMeverFolder() }

    val getButtonClickCount = dataStore.clickCount.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 0
    )

    val adsThreshold = dataStore.adsThreshold.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(),
        initialValue = 3
    )

    private val _backgroundRemovalState = MutableStateFlow<UiState<Bitmap>>(StateInitial)
    val backgroundRemovalState = _backgroundRemovalState.asStateFlow()

    private val _saveImageState = MutableStateFlow<UiState<SaveResult>>(StateInitial)
    val saveImageState = _saveImageState.asStateFlow()

    private val _selectedBackground =
        MutableStateFlow<BgRemovalBackground>(BgRemovalBackground.Transparent)
    val selectedBackground = _selectedBackground.asStateFlow()

    fun selectBackground(background: BgRemovalBackground) {
        _selectedBackground.value = background
    }

    fun loadBackgroundBitmap(context: Context, uri: Uri) {
        viewModelScope.launch {
            val bitmap = decodeResizedBitmap(context.contentResolver, uri, 1024, 1024)
            if (bitmap != null) {
                _selectedBackground.value = BgRemovalBackground.Image(uri, bitmap)
            }
        }
    }

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

    @OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun saveImage(context: Context, bitmap: Bitmap) {
        val timeStamp = changeToCurrentDate(currentTimeMillis())
        val fileName = "MEVER_$timeStamp.png"
        var merged: Bitmap? = null

        collectApiAsUiState(
            response = flow {
                val mergedBitmap = mergeWithBackground(bitmap)
                merged = mergedBitmap
                emit(mergedBitmap)
            }.flatMapLatest { mergedBitmap ->
                repository.uploadImage(mergedBitmap, fileName)
                    .timeout(20.seconds)
                    .catch { e ->
                        if (e is TimeoutCancellationException) {
                            emit(ApiState.Error(Throwable("Fetch timeout")))
                        } else {
                            throw e
                        }
                    }
            },
            onLoading = { _saveImageState.value = StateLoading },
            onSuccess = { url: String? ->
                if (url != null) {
                    ketch.download(
                        url = url,
                        path = meverFolder.absolutePath,
                        fileName = fileName,
                        tag = AI.platformName,
                        metaData = url
                    )
                    _saveImageState.value = StateSuccess(SaveResult(IN_APP, fileName))
                } else {
                    merged?.let { saveImageLocally(context, it, fileName) }
                }
            },
            onFailed = {
                merged?.let {
                    saveImageLocally(
                        context,
                        it,
                        fileName
                    )
                }
            },
            onReset = { _saveImageState.value = StateInitial }
        )
    }

    fun reset() {
        _backgroundRemovalState.value = StateInitial
        _saveImageState.value = StateInitial
        _selectedBackground.value = BgRemovalBackground.Transparent
    }

    fun incrementClickCount() = viewModelScope.launch {
        dataStore.incrementClickCount()
    }

    fun saveToCache(context: Context, bitmap: Bitmap, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val mergedBitmap = mergeWithBackground(bitmap)
            withContext(IO) {
                context.cacheDir.listFiles { file ->
                    file.name.startsWith("temp_bg_removal_") && file.name.endsWith(".png")
                }?.forEach { it.delete() }
            }

            val fileName = "temp_bg_removal_${currentTimeMillis()}.png"
            val cacheFile = File(context.cacheDir, fileName)
            val isSuccess = withContext(IO) {
                saveBitmapToFile(mergedBitmap, cacheFile, true)
            }
            if (isSuccess) {
                onResult(cacheFile.absolutePath)
            } else {
                onResult(null)
            }
        }
    }

    private suspend fun mergeWithBackground(bitmap: Bitmap) = withContext(IO) {
        when (val bg = _selectedBackground.value) {
            is BgRemovalBackground.Color -> bitmap.addBackground(bg.color)
            is BgRemovalBackground.Image -> bg.bitmap?.let {
                bitmap.addBackground(it)
            } ?: bitmap

            else -> bitmap
        }
    }

    private suspend fun saveImageLocally(context: Context, bitmap: Bitmap, fileName: String) {
        val destFile = File(meverFolder, fileName)
        val isSuccess = withContext(IO) {
            saveBitmapToFile(bitmap, destFile, true)
        }

        if (isSuccess) {
            _saveImageState.value = StateSuccess(SaveResult(GALLERY, fileName))
        } else {
            _saveImageState.value = StateFailed(context.getString(R.string.failed_save_image))
        }
    }

    data class SaveResult(
        val location: ImageLocation,
        val fileName: String
    )

    enum class ImageLocation {
        IN_APP, GALLERY
    }

    sealed class BgRemovalBackground {
        object Transparent : BgRemovalBackground()
        data class Color(val color: Int) : BgRemovalBackground()
        data class Image(val uri: Uri, val bitmap: Bitmap? = null) : BgRemovalBackground()
    }
}