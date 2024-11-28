package com.dapascript.mever.core.common.util

import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.util.Patterns.WEB_URL
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar.getInstance
import java.util.Locale.ROOT
import java.util.Locale.getDefault

fun String.getPlatformType(): PlatformType {
    val listFbUrl = listOf("facebook.com", "fb.com", "m.facebook.com", "facebook", "fb")
    val listInstagramUrl = listOf("instagram.com", "instagr.am", "ig.com", "instagram", "ig")
    val listTwitterUrl = listOf("x.com", "twitter.com", "t.co", "twitter")

    return when {
        listFbUrl.any { contains(it) } -> FACEBOOK
        listInstagramUrl.any { contains(it) } -> INSTAGRAM
        listTwitterUrl.any { contains(it) } -> TWITTER
        else -> UNKNOWN
    }
}

fun String.isValidUrl() = WEB_URL.matcher(this).matches()

fun calculateDownloadedMegabytes(progress: Int, totalBytes: Long): String {
    val downloadedBytes = progress / 100.0 * totalBytes
    return getTwoDecimals(value = downloadedBytes / (1024.0 * 1024.0))
}

fun getTwoDecimals(value: Double) = String.format(ROOT, "%.2f", value)

fun calculateDownloadPercentage(downloadedBytes: Long, totalBytes: Long): String {
    if (totalBytes == 0L) return "0%" // Prevent division by zero
    val percentage = downloadedBytes / totalBytes.toDouble() * 100
    return percentage.toInt().toString() + "%"
}

suspend fun getVideoThumbnail(url: String) = withContext(IO) {
    val retriever = MediaMetadataRetriever()
    try {
        // Check if URL is accessible
        val accessible = isUrlAccessible(url)
        if (!accessible) {
            Log.e("VideoThumbnail", "URL is not accessible")
            return@withContext null
        }

        // Set data source
        retriever.setDataSource(url, HashMap())

        // Check video duration
        val duration = retriever.extractMetadata(METADATA_KEY_DURATION)?.toLongOrNull()
        if (duration == null || duration <= 0) {
            Log.e("VideoThumbnail", "Invalid video duration")
            return@withContext null
        }

        synchronized(this) { retriever.getFrameAtTime(2000000, OPTION_CLOSEST_SYNC) }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}

suspend fun isUrlAccessible(url: String) = withContext(IO) {
    try {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()
        connection.responseCode == HTTP_OK
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun getContentType(path: String) = when {
    path.endsWith(".jpg") -> "image/jpeg"
    path.endsWith(".png") -> "image/png"
    else -> "video/mp4"
}

fun Long.toCurrentDate(): String {
    val calendar = getInstance()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy • HH:mm aaa", getDefault())
    calendar.timeInMillis = this
    return dateFormat.format(calendar.time)
}

fun getMeverFolder() = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER")