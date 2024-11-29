package com.dapascript.mever.core.common.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Patterns.WEB_URL
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.core.content.FileProvider.getUriForFile
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
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

suspend fun getVideoThumbnail(source: String) = withContext(IO) {
    val retriever = MediaMetadataRetriever()
    try {
        retriever.dataSource(source)
        synchronized(this) { retriever.getFrameAtTime(2000000, OPTION_CLOSEST_SYNC) }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}

fun MediaMetadataRetriever.dataSource(source: String) {
    if (source.isValidUrl()) setDataSource(source, HashMap())
    else setDataSource(source)
}

fun getContentType(path: String) = when {
    path.endsWith(".jpg") -> "image/jpeg"
    path.endsWith(".png") -> "image/png"
    else -> "video/mp4"
}

fun Long.toCurrentDate(): String {
    val calendar = getInstance()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy • HH.mm.ss", getDefault())
    calendar.timeInMillis = this
    return dateFormat.format(calendar.time)
}

fun getMeverFolder() = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER")

fun getMeverFiles(): List<File>? {
    val meverFolder = getMeverFolder()
    return if (meverFolder.exists() && meverFolder.isDirectory) {
        meverFolder.listFiles { file ->
            file.isFile && file.extension.lowercase() in listOf(
                "mp4",
                "mkv",
                "avi",
                "mov",
                "flv",
                "wmv"
            )
        }?.toList()
    } else emptyList()
}

fun shareContent(context: Context, authority: String, path: String) {
    try {
        if (getMeverFolder().exists()) {
            val uri = getUriForFile(context, authority, File(path))
            IntentBuilder(context)
                .setType(getContentType(path))
                .setSubject("Shared from Mever")
                .addStream(uri)
                .setChooserTitle("Shared Video")
                .startChooser()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}