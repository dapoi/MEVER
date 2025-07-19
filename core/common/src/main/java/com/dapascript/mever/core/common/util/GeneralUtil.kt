package com.dapascript.mever.core.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeStream
import android.media.MediaMetadataRetriever
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS
import android.provider.Settings.EXTRA_APP_PACKAGE
import android.util.Patterns.WEB_URL
import android.webkit.MimeTypeMap.getSingleton
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.app.ShareCompat.IntentBuilder
import androidx.core.content.FileProvider.getUriForFile
import androidx.core.net.toUri
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.dapascript.mever.core.common.util.Constant.PlatformType.YOUTUBE
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
            if (isValidUrl(source)) setDataSource(source, HashMap()) else setDataSource(source)
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
    path.isEmpty() -> "..."
    isVideo(path) -> "video/mp4"
    else -> "image/jpg"
}

fun getContentTypeFromFile(file: File) =
    getSingleton().getMimeTypeFromExtension(file.extension.lowercase())

fun getMeverFolder(): File {
    val folder = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER")
    if (folder.exists().not()) folder.mkdirs()
    return folder
}

fun getMeverFiles(): List<File>? {
    val meverFolder = getMeverFolder()
    return if (meverFolder.exists() && meverFolder.isDirectory) {
        meverFolder.listFiles { file ->
            file.isFile && file.extension.lowercase() in listOf("mp4", "jpg")
        }?.toList()
    } else emptyList()
}

fun getFilePath(fileName: String) = getMeverFiles()?.find { it.name == fileName }?.path.orEmpty()

fun isAvailableOnLocal(fileName: String) = getMeverFiles()?.any {
    it.name == fileName
} ?: false

fun shareContent(context: Context, file: File) {
    try {
        val uri = getUriForFile(
            /* context = */ context,
            /* authority = */ "${context.packageName}.fileprovider",
            /* file = */ file
        )
        IntentBuilder(context)
            .setType(getContentTypeFromFile(file))
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
            data = "mailto:".toUri()
            putExtra(EXTRA_EMAIL, arrayOf("daffaprabowo5@gmail.com"))
            putExtra(EXTRA_SUBJECT, "MEVER Feedback")
            putExtra(EXTRA_TEXT, "Hello, I would like to share my feedback about MEVER.")
        }
        context.startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(context, R.string.gmail_not_found, LENGTH_SHORT).show()
    }
}

fun convertToTimeFormat(milliseconds: Long): String {
    val seconds = milliseconds / 1000
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

fun getPlatformType(url: String): PlatformType {
    val listFbUrl = listOf("facebook.com", "fb.com", "m.facebook.com", "fb.watch")
    val listInstagramUrl = listOf("instagram.com", "instagr.am", "ig.com")
    val listTwitterUrl = listOf("x.com", "twitter.com", "t.co", "mobile.twitter.com")
    val listTiktokUrl = listOf("tiktok.com", "tiktokv.com", "tiktokcdn.com")
    val listYouTubeUrl = listOf("youtube.com", "youtu.be", "m.youtube.com", "yt.com")

    return when {
        listFbUrl.any { url.contains(it) } -> FACEBOOK
        listInstagramUrl.any { url.contains(it) } -> INSTAGRAM
        listTwitterUrl.any { url.contains(it) } -> TWITTER
        listTiktokUrl.any { url.contains(it) } -> TIKTOK
        listYouTubeUrl.any { url.contains(it) } -> YOUTUBE
        else -> UNKNOWN
    }
}

fun isValidUrl(url: String) = WEB_URL.matcher(url).matches()

fun isVideo(path: String) = path.endsWith(".mp4")

fun changeToCurrentDate(date: Long): String {
    val calendar = getInstance()
    val dateFormat = SimpleDateFormat("yyyy.MM.dd - HH_mm_ss", getDefault())
    calendar.timeInMillis = date
    return dateFormat.format(calendar.time)
}

fun hideSystemBar(activity: Activity, value: Boolean) = with(activity) {
    val insetsController = getInsetsController(window, window.decorView)
    if (value) insetsController.hide(systemBars()) else insetsController.show(systemBars())
}

fun convertFilename(filename: String): String {
    val regex = Regex("""(\d{4})\.(\d{2})\.(\d{2}) - (\d{2})_(\d{2})_(\d{2})\.(\w+)""")
    val match = regex.find(filename)

    return if (match != null) {
        val (year, month, day, hour, minute, second, ext) = match.destructured
        val newDate = "$day.$month.$year"
        val newTime = "$hour:$minute:$second"
        "$newDate - $newTime.$ext"
    } else filename
}

fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "1.0"
    } catch (e: Exception) {
        e.printStackTrace()
        "1.0"
    }
}