package com.dapascript.mever.core.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeStream
import android.media.MediaMetadataRetriever
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import android.provider.Settings.EXTRA_APP_PACKAGE
import android.util.Patterns.WEB_URL
import android.widget.Toast
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus
import com.dapascript.mever.core.common.util.connectivity.ConnectivityObserver.NetworkStatus.Available
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

suspend fun getPhotoThumbnail(url: String) = withContext(IO) {
    try {
        decodeStream(URL(url).openStream())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun getVideoThumbnail(source: String): Bitmap? = withContext(IO) {
    val retriever = MediaMetadataRetriever()
    try {
        with(retriever) {
            if (source.isValidUrl()) setDataSource(source, HashMap()) else setDataSource(source)
            getFrameAtTime(100000)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}

suspend fun getUrlContentType(url: String) = withContext(IO) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    try {
        val type = client.newCall(request).execute()
        if (type.body?.contentType().toString() == "video/mp4") ".mp4" else ".jpg"
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

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

fun getContentType(path: String) = when {
    path.isEmpty() -> "in progress"
    path.isVideo() -> "video/mp4"
    else -> "image/jpg"
}

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

fun String.convertToBitmap(): Bitmap? {
    return try {
        val inputStream = URL(this).openStream()
        decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun shareContent(context: Context, authority: String, path: String) {
    try {
        val uri = getUriForFile(context, "$authority.provider", File(path))
        IntentBuilder(context)
            .setType(getContentType(path))
            .setSubject("MEVER Shared Content")
            .addStream(uri)
            .setChooserTitle("Share with")
            .startChooser()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun navigateToNotificationSettings(context: Context) {
    try {
        context.startActivity(
            Intent(ACTION_APP_NOTIFICATION_SETTINGS).putExtra(
                EXTRA_APP_PACKAGE,
                context.packageName
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun navigateToGmail(context: Context) {
    try {
        val intent = Intent(ACTION_SENDTO).apply {
            type = "text/plain"
            putExtra(EXTRA_EMAIL, arrayOf("luthfidaffaprabowo@gmail.com"))
            putExtra(EXTRA_SUBJECT, "MEVER Feedback")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, R.string.gmail_not_found, Toast.LENGTH_SHORT).show()
    }
}

fun getNetworkStatus(
    isNetworkAvailable: NetworkStatus,
    onNetworkAvailable: () -> Unit,
    onNetworkUnavailable: () -> Unit
) = when (isNetworkAvailable) {
    Available -> onNetworkAvailable()
    else -> onNetworkUnavailable()
}

fun Long.toTimeFormat(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return when {
        hours > 0 -> String.format(
            getDefault(),
            "%02d:%02d:%02d",
            hours,
            minutes % 60,
            seconds % 60
        )

        else -> String.format(getDefault(), "%02d:%02d", minutes, seconds % 60)
    }
}

fun String.getPlatformType(): PlatformType {
    val listFbUrl = listOf("facebook.com", "fb.com", "m.facebook.com", "fb.watch")
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

fun String.isVideo() = endsWith(".mp4")

fun Long.toCurrentDate(): String {
    val calendar = getInstance()
    val dateFormat = SimpleDateFormat("yyyy.MM.dd - HH_mm", getDefault())
    calendar.timeInMillis = this
    return dateFormat.format(calendar.time)
}

fun String.replaceTimeFormat() = replace("_", ".")

fun Activity.hideSystemBar(value: Boolean) {
    val insetsController = getInsetsController(window, window.decorView)
    if (value) {
        insetsController.hide(systemBars())
        insetsController.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        insetsController.show(systemBars())
        insetsController.systemBarsBehavior = BEHAVIOR_DEFAULT
    }
}

fun DownloadModel.isAvailableOnLocal() = getMeverFiles()?.any {
    it.name == fileName
} ?: false