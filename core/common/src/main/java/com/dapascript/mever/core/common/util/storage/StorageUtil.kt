package com.dapascript.mever.core.common.util.storage

import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.Context.STORAGE_SERVICE
import android.content.Context.STORAGE_STATS_SERVICE
import android.media.MediaScannerConnection
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.storage.StorageManager
import android.os.storage.StorageManager.UUID_DEFAULT
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

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
        val storageManager = context.getSystemService(STORAGE_SERVICE) as StorageManager
        val statsManager = context.getSystemService(STORAGE_STATS_SERVICE) as StorageStatsManager

        val storageVolume = storageManager.primaryStorageVolume
        val uuidStr = storageVolume.uuid ?: UUID_DEFAULT.toString()
        val uuid = UUID.fromString(uuidStr)

        val totalBytes = statsManager.getTotalBytes(uuid)
        val freeBytes = statsManager.getFreeBytes(uuid)
        val usedBytes = totalBytes - freeBytes

        val usedPercent = ((usedBytes.toDouble() / totalBytes.toDouble()) * 100).toInt()
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

    suspend fun getMeverFiles(): List<File>? = withContext(IO) {
        val dir = getMeverFolder()
        if ((dir.exists() && dir.isDirectory).not()) return@withContext emptyList()
        dir.listFiles()?.asSequence()
            ?.filter { it.isFile && it.extension.lowercase() in allowExt }
            ?.sortedByDescending { it.lastModified() }
            ?.toList()
            ?: emptyList()
    }

    suspend fun getFilePath(fileName: String): String = withContext(IO) {
        val file = File(getMeverFolder(), fileName)
        if (file.exists()) file.absolutePath else ""
    }

    suspend fun syncFileToGallery(context: Context, fileName: String) {
        val file = File(getFilePath(fileName))
        if (file.exists()) MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            null,
            null
        )
    }
}