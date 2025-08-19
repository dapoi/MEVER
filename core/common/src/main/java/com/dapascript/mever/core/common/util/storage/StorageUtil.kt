package com.dapascript.mever.core.common.util.storage

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File

object StorageUtil {

    private val allowExt = setOf("mp4", "mp3", "jpg")

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