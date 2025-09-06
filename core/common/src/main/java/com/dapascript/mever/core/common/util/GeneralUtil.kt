package com.dapascript.mever.core.common.util

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.ACTION_VIEW
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.BitmapFactory.decodeStream
import android.media.MediaMetadataRetriever
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.Q
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.provider.MediaStore.MediaColumns.MIME_TYPE
import android.provider.MediaStore.MediaColumns.RELATIVE_PATH
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
import com.dapascript.mever.core.common.util.PlatformType.ALL
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.SPOTIFY
import com.dapascript.mever.core.common.util.PlatformType.TERABOX
import com.dapascript.mever.core.common.util.PlatformType.THREADS
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.PlatformType.VIDEY
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE_MUSIC
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.OutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar.getInstance
import java.util.Locale.ROOT
import java.util.Locale.getDefault

suspend fun fetchPhotoFromUrl(url: String) = withContext(IO) {
    try {
        decodeStream(URL(url).openStream())
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun fetchVideoThumbnail(source: String): Bitmap? = withContext(IO) {
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

suspend fun getExtensionFromUrl(url: String, extensionFile: String) = withContext(IO) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    try {
        val client = client.newCall(request).execute()
        val contentType = client.body?.contentType()?.toString() ?: ""
        if (extensionFile.isEmpty()) when {
            contentType.contains("video") || contentType.contains("mp4") -> ".mp4"
            contentType.contains("audio") -> ".mp3"
            else -> ".jpg"
        } else if (extensionFile == "All Images") ".jpg" else ".$extensionFile"
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

suspend fun saveBitmapToStorage(
    context: Context,
    bitmap: Bitmap,
    fileName: String
) = withContext(IO) {
    val values = ContentValues().apply {
        put(DISPLAY_NAME, fileName)
        put(MIME_TYPE, "image/jpeg")
        if (SDK_INT >= Q) put(RELATIVE_PATH, DIRECTORY_PICTURES)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(EXTERNAL_CONTENT_URI, values)

    try {
        uri?.let {
            val outputStream: OutputStream? = resolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(JPEG, 100, stream)
            }
            true
        } ?: false
    } catch (e: Exception) {
        e.printStackTrace()
        false
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

fun shareContent(context: Context, files: List<File>) {
    try {
        if (files.isEmpty()) return

        val uris = files.map { file ->
            getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        }
        val mimes = files.map { getContentTypeFromFile(it) ?: "*/*" }.toSet()
        val mime = if (mimes.size == 1) {
            mimes.first()
        } else {
            val top = mimes.map { it.substringBefore('/') }.toSet().filter { it.isNotBlank() }
            if (top.size == 1) "${top.first()}/*" else "*/*"
        }

        IntentBuilder(context)
            .setType(mime)
            .setSubject("MEVER Shared Content")
            .setChooserTitle("Share with")
            .apply {
                uris.forEach { addStream(it) }
            }
            .startChooser()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun shareContent(context: Context, file: File) {
    try {
        val uri = getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
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

fun navigateToMusic(context: Context, file: File) {
    try {
        val uri = getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(ACTION_VIEW).apply {
            setDataAndType(uri, getContentTypeFromFile(file))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(intent)
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

fun navigateToBrowser(context: Context, url: String) {
    val intent = Intent(ACTION_VIEW, url.toUri())
    context.startActivity(intent)
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

fun getPlatformType(url: String, type: String = "video"): PlatformType {
    val listFbUrl = listOf("facebook.com", "fb.com", "m.facebook.com", "fb.watch")
    val listInstagramUrl = listOf("instagram.com", "instagr.am", "ig.com")
    val listPinterestUrl = listOf("pinterest.com", "pin.it", "pinterest.co.uk")
    val listSpotifyUrl = listOf("spotify.com", "spoti.fi", "spotify.link")
    val listTeraboxUrl = listOf("terabox.com", "terabox.co", "terabox.net")
    val listThreadsUrl = listOf("threads.net", "threadsthis.com", "threadsthis.net", "threads.com")
    val listTiktokUrl = listOf("tiktok.com", "tiktokv.com", "tiktokcdn.com")
    val listTwitterUrl = listOf("x.com", "twitter.com", "t.co", "mobile.twitter.com")
    val listVideyUrl = listOf("videy.com", "videy.net", "videy.co")
    val listYouTubeUrl = listOf("youtube.com", "youtu.be", "m.youtube.com", "yt.com")

    return when {
        listYouTubeUrl.any { url.contains(it) } && type.contains("audio") -> YOUTUBE_MUSIC
        listFbUrl.any { url.contains(it) } -> FACEBOOK
        listInstagramUrl.any { url.contains(it) } -> INSTAGRAM
        listPinterestUrl.any { url.contains(it) } -> PINTEREST
        listSpotifyUrl.any { url.contains(it) } -> SPOTIFY
        listTeraboxUrl.any { url.contains(it) } -> TERABOX
        listThreadsUrl.any { url.contains(it) } -> THREADS
        listTiktokUrl.any { url.contains(it) } -> TIKTOK
        listTwitterUrl.any { url.contains(it) } -> TWITTER
        listVideyUrl.any { url.contains(it) } -> VIDEY
        listYouTubeUrl.any { url.contains(it) } -> YOUTUBE
        else -> ALL
    }
}

fun isValidUrl(url: String) = WEB_URL.matcher(url).matches()

fun isVideo(path: String) = path.endsWith(".mp4")

fun isMusic(path: String) = path.endsWith(".mp3")

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

fun isSystemBarVisible(activity: Activity): Boolean {
    val insetsController = getInsetsController(activity.window, activity.window.decorView)
    return insetsController.systemBarsBehavior and systemBars() != 0
}

fun convertFilename(filename: String): String {
    val regex =
        Regex("""(\d{4})\.(\d{2})\.(\d{2}) - (\d{2})_(\d{2})_(\d{2})(?: \((\d+)\))?\.(\w+)""")
    val match = regex.find(filename)

    return if (match != null) {
        val (year, month, day, hour, minute, second, duplicate, ext) = match.destructured
        val newDate = "$day-$month-$year"
        val newTime = "$hour:$minute:$second"
        val dupSuffix = if (duplicate.isNotEmpty()) " ($duplicate)" else ""
        "$newDate • $newTime$dupSuffix.$ext"
    } else filename
}

fun sanitizeFilename(filename: String): String {
    val illegalCharsRegex = Regex("[^a-zA-Z0-9 ._-]")
    return illegalCharsRegex.replace(filename, "_")
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