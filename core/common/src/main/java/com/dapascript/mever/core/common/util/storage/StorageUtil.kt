package com.dapascript.mever.core.common.util.storage

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import java.io.File

object StorageUtil {

    private val allowExt = setOf("mp4", "mp3", "jpg")

    fun getMeverFolder(): File {
        val folder = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER")
        if (folder.exists().not()) folder.mkdirs()
        return folder
    }

    fun getMeverFiles(): List<File>? {
        val dir = getMeverFolder()
        if ((dir.exists() && dir.isDirectory).not()) return emptyList()
        return dir.listFiles()?.asSequence()
            ?.filter { it.isFile && it.extension.lowercase() in allowExt }
            ?.sortedByDescending { it.lastModified() }
            ?.toList()
            ?: emptyList()
    }

    fun getFilePath(fileName: String) = runCatching {
        getMeverFiles()
            ?.find { it.name == fileName }
            ?.path.orEmpty()
    }.getOrDefault("")

    fun syncFileToGallery(context: Context, fileName: String) {
        val file = File(getFilePath(fileName))
        if (file.exists()) MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            null,
            null
        )
    }
}