package com.dapascript.mever.core.common.util

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory.decodeStream
import android.media.MediaMetadataRetriever
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Patterns.WEB_URL
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.Status
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.Status.Available
import com.ketch.DownloadModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar.getInstance
import java.util.Locale.ROOT
import java.util.Locale.getDefault
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

fun String.getPlatformType(): PlatformType {
    val listFbUrl = listOf("facebook.com", "fb.com", "m.facebook.com")
    val listInstagramUrl = listOf("instagram.com", "instagr.am", "ig.com")
    val listTwitterUrl = listOf("x.com", "twitter.com", "t.co", "mobile.twitter.com")
    val listTiktokUrl = listOf("tiktok.com", "tiktokv.com", "tiktokcdn.com")
    val listYouTubeUrl = listOf("youtube.com", "youtu.be", "m.youtube.com", "yt.com")

    return when {
        listFbUrl.any { contains(it) } -> FACEBOOK
        listInstagramUrl.any { contains(it) } -> INSTAGRAM
        listTwitterUrl.any { contains(it) } -> TWITTER
        listTiktokUrl.any { contains(it) } -> TIKTOK
        listYouTubeUrl.any { contains(it) } -> YOUTUBE
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

suspend fun getPhotoThumbnail(url: String) = withContext(IO) {
    try {
        decodeStream(URL(url).openStream())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun getVideoThumbnail(source: String) = withContext(IO) {
    val retriever = MediaMetadataRetriever()
    try {
        retriever.dataSource(source)
        retriever.getFrameAtTime(100000)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}

private fun MediaMetadataRetriever.dataSource(source: String) {
    if (source.isValidUrl()) setDataSource(source, HashMap())
    else setDataSource(source)
}

suspend fun getUrlContentType(url: String) = withContext(IO) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .head()
        .build()

    try {
        val type = client.newCall(request).execute().use { response -> response.header("Content-Type") }
        if (type == "application/octet-stream" || type == "video/mp4") ".mp4" else ".jpg"
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getLocalContentType(path: String) = if (path.endsWith(".jpg")) "image/*" else "video/*"

fun Long.toCurrentDate(): String {
    val calendar = getInstance()
    val dateFormat = SimpleDateFormat("MMM dd, yyyy • HH_mm_ss", getDefault())
    calendar.timeInMillis = this
    return dateFormat.format(calendar.time)
}

fun String.replaceTimeFormat() = replace("_", ":")

fun getMeverFolder() = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER")

fun getMeverFiles(): List<File>? {
    val meverFolder = getMeverFolder()
    return if (meverFolder.exists() && meverFolder.isDirectory) {
        meverFolder.listFiles { file ->
            file.isFile && file.extension.lowercase() in listOf("mp4", "jpg")
        }?.toList()
    } else emptyList()
}

@OptIn(ExperimentalPathApi::class)
fun deleteAllMeverFolder() = getMeverFolder().apply { if (exists()) toPath().deleteRecursively() }

fun shareContent(context: Context, authority: String, path: String) {
    try {
        val uri = getUriForFile(context, "$authority.provider", File(path))
        IntentBuilder(context)
            .setType(getLocalContentType(path))
            .setSubject("MEVER Shared Content")
            .addStream(uri)
            .setChooserTitle("Share with")
            .startChooser()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Long.toTimeFormat(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return when {
        hours > 0 -> String.format(getDefault(), "%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
        else -> String.format(getDefault(), "%02d:%02d", minutes, seconds % 60)
    }
}

fun getNetworkStatus(
    isNetworkAvailable: Status,
    onNetworkAvailable: () -> Unit,
    onNetworkUnavailable: () -> Unit
) = when (isNetworkAvailable) {
    Available -> onNetworkAvailable()
    else -> onNetworkUnavailable()
}

fun Activity.hideStatusBar(value: Boolean) {
    val insetsController = getInsetsController(window, window.decorView)
    if (value) {
        insetsController.hide(systemBars())
        insetsController.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        insetsController.show(systemBars())
        insetsController.systemBarsBehavior = BEHAVIOR_DEFAULT
    }
}

fun DownloadModel.isAvailableOnLocal() = getMeverFiles()?.map { it.name }.orEmpty().contains(fileName)