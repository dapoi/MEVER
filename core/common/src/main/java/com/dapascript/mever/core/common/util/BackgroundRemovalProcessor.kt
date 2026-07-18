package com.dapascript.mever.core.common.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.net.Uri
import androidx.core.graphics.scale
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage.fromBitmap
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.max

class BackgroundRemovalProcessor @Inject constructor() {

    /**
     * Removes the background from the image at [imageUri] using ML Kit Subject Segmentation.
     * Returns a new [Bitmap] with transparent background, or null if processing fails.
     */
    suspend fun removeBackground(
        contentResolver: ContentResolver,
        imageUri: Uri
    ): Bitmap? = withContext(IO) {
        // decodeResizedBitmap uses inSampleSize (power-of-2 rough downscale),
        // toProcessingBitmap then does the precise final scale + ensures ARGB_8888
        val source = decodeResizedBitmap(
            contentResolver = contentResolver,
            uri = imageUri,
            reqWidth = MAX_SIZE,
            reqHeight = MAX_SIZE
        )?.toProcessingBitmap() ?: return@withContext null

        val options = SubjectSegmenterOptions.Builder()
            .enableForegroundBitmap()
            .build()
        val segmenter = SubjectSegmentation.getClient(options)

        try {
            val result = segmenter.process(fromBitmap(source, 0)).await()
            result.foregroundBitmap
        } catch (_: Exception) {
            null
        } finally {
            source.recycle()
            segmenter.close()
        }
    }

    /**
     * Ensures bitmap is ARGB_8888 and within [MAX_SIZE] bounds.
     * Recycles the original bitmap when a new one is created.
     */
    private fun Bitmap.toProcessingBitmap(): Bitmap? {
        val largestSide = max(width, height)

        // Already within bounds and correct config — return as-is
        if (largestSide <= MAX_SIZE && config == ARGB_8888) return this

        val ratio = if (largestSide > MAX_SIZE) MAX_SIZE.toFloat() / largestSide else 1f
        val newW = (width * ratio).toInt().coerceAtLeast(1)
        val newH = (height * ratio).toInt().coerceAtLeast(1)

        // When largestSide > MAX_SIZE, dimensions always differ from original
        // so createScaledBitmap guarantees a new instance — recycle() is safe.
        // When only config conversion is needed, copy() also creates a new instance.
        val processed = if (largestSide > MAX_SIZE) {
            this.scale(newW, newH)
        } else {
            copy(ARGB_8888, false)
        }

        recycle()
        return processed
    }

    /**
     * Converts a GMS [Task] to a suspend function with proper cancellation support.
     */
    private suspend fun <T> Task<T>.await(): T =
        suspendCancellableCoroutine { cont ->
            addOnSuccessListener { cont.resume(it) }
            addOnFailureListener { e ->
                if (cont.isActive) cont.resumeWithException(e)
            }
            addOnCanceledListener {
                if (cont.isActive) cont.cancel()
            }
        }

    private companion object {
        const val MAX_SIZE = 1024
    }
}
