package com.dapascript.mever.core.common.util

import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Patterns.WEB_URL
import com.dapascript.mever.core.common.util.Constant.PlatformType
import com.dapascript.mever.core.common.util.Constant.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformType.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformType.UNKNOWN
import com.ketch.Ketch
import java.util.Locale.ROOT

fun String.getPlatformType(): PlatformType {
    val listFbUrl = listOf("facebook.com", "fb.com", "m.facebook.com")
    val listInstagramUrl = listOf("instagram.com", "instagr.am")
    val listTwitterUrl = listOf("x.com")

    return when {
        listFbUrl.any { contains(it) } -> FACEBOOK
        listInstagramUrl.any { contains(it) } -> INSTAGRAM
        listTwitterUrl.any { contains(it) } -> TWITTER
        else -> UNKNOWN
    }
}

fun String.isValidUrl() = WEB_URL.matcher(this).matches()

fun downloadFile(ketch: Ketch, url: String) = ketch.download(
    url = url,
    path = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).path,
    fileName = "Mever Video ${System.currentTimeMillis()}"
)

fun calculateDownloadedMegabytes(progress: Int, totalBytes: Long): String {
    val downloadedBytes = progress / 100.0 * totalBytes
    return getTwoDecimals(value = downloadedBytes / (1024.0 * 1024.0))
}

fun getTwoDecimals(value: Double) = String.format(ROOT, "%.2f", value)