package com.dapascript.mever.core.common.util.storage

import android.content.Context
import android.media.MediaScannerConnection
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.os.Environment.getExternalStoragePublicDirectory
import java.io.File

object StorageUtil {
    fun getMeverFolder(): File {
        val folder = File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER")
        if (folder.exists().not()) folder.mkdirs()
        return folder
    }

    fun getMeverFolder(hideFromGallery: Boolean = false): File {
        val baseFolder = File(
            getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS), "MEVER"
        )

        val folder = if (hideFromGallery) {
            val hiddenFolder = File(baseFolder, "hidden")
            if (!hiddenFolder.exists()) hiddenFolder.mkdirs()

            val nomediaFile = File(hiddenFolder, ".nomedia")
            if (!nomediaFile.exists()) nomediaFile.createNewFile()

            hiddenFolder
        } else {
            if (!baseFolder.exists()) baseFolder.mkdirs()
            baseFolder
        }

        return folder
    }

    fun getMeverFiles(): List<File>? {
        val meverFolder = getMeverFolder()
        return if (meverFolder.exists() && meverFolder.isDirectory) {
            meverFolder.listFiles { file ->
                file.isFile && file.extension.lowercase() in listOf("mp4", "mp3", "jpg")
            }?.toList()
        } else emptyList()
    }

    fun getFilePath(fileName: String) = getMeverFiles()?.find {
        it.name == fileName
    }?.path.orEmpty()

    fun isAvailableOnLocal(fileName: String) = getMeverFiles()?.any {
        it.name == fileName
    } ?: false

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