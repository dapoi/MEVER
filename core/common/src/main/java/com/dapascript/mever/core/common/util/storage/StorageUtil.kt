package com.dapascript.mever.core.common.util.storage

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.R
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.StatFs
import android.os.storage.StorageManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.roundToInt

object StorageUtil {

    private val allowExt = setOf("mp4", "mp3", "jpg")

    data class StorageInfo(
        val totalBytes: Long,
        val freeBytes: Long,
        val usedBytes: Long,
        val usedPercent: Int,
        val freePercent: Int
    )

    fun getStorageInfo(context: Context): StorageInfo {
        val storageManager = context.getSystemService(StorageManager::class.java)

        val primaryDir = if (SDK_INT >= R) {
            storageManager.primaryStorageVolume.directory
        } else {
            @Suppress("DEPRECATION")
            Environment.getExternalStorageDirectory()
        } ?: Environment.getDataDirectory()
        val statFs = StatFs(primaryDir.absolutePath)
        val totalBytes = statFs.totalBytes
        val freeBytes = statFs.availableBytes
        val usedBytes = (totalBytes - freeBytes).coerceAtLeast(0L)
        val usedPercent = if (totalBytes > 0L) {
            ((usedBytes * 100.0) / totalBytes).roundToInt()
        } else 0
        val freePercent = 100 - usedPercent

        return StorageInfo(
            totalBytes = totalBytes,
            freeBytes = freeBytes,
            usedBytes = usedBytes,
            usedPercent = usedPercent,
            freePercent = freePercent
        )
    }

    fun isStorageFull(storageInfo: StorageInfo?): Boolean {
        if (storageInfo == null) return false
        val minimumFreeBytes = 500 * 1024 * 1024
        return storageInfo.freeBytes < minimumFreeBytes
    }

    fun getMeverFolder(): File {
        val folder = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER")
        if (folder.exists().not()) folder.mkdirs()
        return folder
    }

    suspend fun getMeverFiles(dir: File): List<File> = withContext(IO) {
        if ((dir.exists() && dir.isDirectory).not()) return@withContext emptyList()
        dir.listFiles()?.asSequence()
            ?.filter { it.isFile && it.extension.lowercase() in allowExt }
            ?.toList()
            ?: emptyList()
    }

    suspend fun getFilePath(dir: File, fileName: String): File? = withContext(IO) {
        File(dir, fileName).takeIf { it.exists() && it.isFile }
    }
}