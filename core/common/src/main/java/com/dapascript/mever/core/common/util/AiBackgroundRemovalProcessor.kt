package com.dapascript.mever.core.common.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.net.Uri
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage.fromBitmap
import com.google.mlkit.vision.segmentation.Segmentation.getClient
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions.SINGLE_IMAGE_MODE
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.nio.ByteOrder
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.math.max

class AiBackgroundRemovalProcessor @Inject constructor() {
    suspend fun removeBackground(
        contentResolver: ContentResolver,
        imageUri: Uri
    ): Bitmap? = withContext(IO) {
        runCatching {
            val source = decodeResizedBitmap(
                contentResolver = contentResolver,
                uri = imageUri,
                reqWidth = 1024,
                reqHeight = 1024
            )?.scaleForProcessing() ?: return@runCatching null

            val options = SelfieSegmenterOptions.Builder()
                .setDetectorMode(SINGLE_IMAGE_MODE)
                .build()
            val segmenter = getClient(options)
            val mask = segmenter.process(fromBitmap(source, 0)).await()
            segmenter.close()
            source.applySegmentationMask(mask)
        }.getOrNull()
    }

    private fun Bitmap.scaleForProcessing(maxSize: Int = 1024): Bitmap {
        val largestSide = max(width, height)
        if (largestSide <= maxSize) return copy(ARGB_8888, true)

        val scale = maxSize.toFloat() / largestSide
        return this.scale(
            (width * scale).toInt().coerceAtLeast(1),
            (height * scale).toInt().coerceAtLeast(1)
        ).copy(ARGB_8888, true)
    }

    private fun Bitmap.applySegmentationMask(mask: SegmentationMask): Bitmap {
        val width = this.width
        val height = this.height
        val result = createBitmap(width, height)
        val pixels = IntArray(width * height)
        val maskBuffer = mask.buffer
        
        maskBuffer.order(ByteOrder.nativeOrder())
        this.getPixels(pixels, 0, width, 0, 0, width, height)
        maskBuffer.rewind()

        for (i in pixels.indices) {
            val confidence = maskBuffer.float
            val alpha = when {
                confidence > 0.9f -> 255
                confidence < 0.8f -> 0
                else -> (((confidence - 0.8f) / 0.1f) * 255).toInt()
            }
            val pixel = pixels[i]
            
            pixels[i] = (pixel and 0x00FFFFFF) or (alpha shl 24)
        }

        result.setPixels(pixels, 0, width, 0, 0, width, height)
        return result
    }

    private suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result -> continuation.resume(result) }
        addOnFailureListener { error ->
            if (continuation.isActive) continuation.resumeWith(Result.failure(error))
        }
        addOnCanceledListener { continuation.cancel() }
    }
}