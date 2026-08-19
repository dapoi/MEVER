package com.dapascript.mever.core.data.repository

import android.graphics.Bitmap
import androidx.work.workDataOf
import com.dapascript.mever.core.common.util.getContentTypeFromFile
import com.dapascript.mever.core.common.util.sanitizeFilename
import com.dapascript.mever.core.common.util.saveBitmapToFile
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFiles
import com.dapascript.mever.core.common.util.storage.StorageUtil.getMeverFolder
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_DOWNLOAD
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_GENERATE_AI
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_PROMPT
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_QUALITY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_TYPE
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_URL
import com.dapascript.mever.core.common.util.worker.WorkerConstant.TYPE_AUDIO
import com.dapascript.mever.core.common.util.worker.WorkerConstant.TYPE_VIDEO
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.base.BaseRepository
import com.dapascript.mever.core.data.repository.base.BaseRepositoryArgs
import com.dapascript.mever.core.data.source.remote.ApiService
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status.CANCELLED
import com.ketch.Status.PROGRESS
import com.ketch.Status.QUEUED
import com.ketch.Status.STARTED
import com.ketch.Status.SUCCESS
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

internal class MeverRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val ketch: Ketch,
    args: BaseRepositoryArgs
) : MeverRepository, BaseRepository(args) {

    private val meverFolder by lazy { getMeverFolder() }

    override fun getAppConfig() = safeApiCall {
        apiService.getAppConfig().mapToEntity()
    }

    override fun getDownloader(
        url: String,
        quality: String
    ) = safeWorkerCall<List<ContentEntity>>(
        serviceType = ACTION_DOWNLOAD,
        requestParam = workDataOf(
            KEY_URL to url,
            KEY_QUALITY to quality,
            KEY_TYPE to if (quality.contains("kbps", true)) TYPE_AUDIO else TYPE_VIDEO
        )
    )

    override fun getImageSearch(query: String) = safeApiCall {
        apiService.getImageSearch(query).mapToEntity() ?: emptyList()
    }

    override fun getImageAiGenerator(prompt: String) = safeWorkerCall<ImageAiEntity>(
        serviceType = ACTION_GENERATE_AI,
        requestParam = workDataOf(
            KEY_PROMPT to prompt
        )
    )

    override fun postReportAiImage(message: String) = safeApiCall {
        apiService.reportAiImage(message)
    }

    override fun uploadImage(bitmap: Bitmap, fileName: String) = safeApiCall {
        val cacheFile = File(context.cacheDir, sanitizeFilename(fileName))
        try {
            val isSaved = saveBitmapToFile(bitmap, cacheFile, true)
            if (isSaved.not()) throw Exception("Failed to save bitmap to cache")

            val reqType = "fileupload".toRequestBody("text/plain".toMediaTypeOrNull())
            val time = "1h".toRequestBody("text/plain".toMediaTypeOrNull())
            val mimeType = getContentTypeFromFile(cacheFile)
            val requestFile = cacheFile.asRequestBody(mimeType?.toMediaTypeOrNull())
            val body = createFormData("fileToUpload", cacheFile.name, requestFile)
            apiService.uploadToLitterbox(
                reqtype = reqType,
                time = time,
                fileToUpload = body
            ).string()
        } finally {
            if (cacheFile.exists()) cacheFile.delete()
        }
    }

    override fun observeDownloads() = ketch.observeDownloads()
        .onEach { downloads ->
            downloads.filter { it.status == CANCELLED }.forEach { ketch.clearDb(it.id) }
        }
        .map { downloads ->
            downloads.filter { it.status != CANCELLED }.map {
                it.copy(
                    path = File(meverFolder, it.fileName).absolutePath
                )
            }.sortedWith(
                compareByDescending<DownloadModel> {
                    it.status in listOf(QUEUED, STARTED, PROGRESS)
                }.thenByDescending { it.timeQueued }
            )
        }

    override fun download(url: String, fileName: String, tag: String, metaData: String) {
        ketch.download(
            url = url,
            path = meverFolder.path,
            fileName = sanitizeFilename(fileName),
            tag = tag,
            metaData = metaData
        )
    }

    override fun pauseDownload(id: Int) = ketch.pause(id)

    override fun resumeDownload(id: Int) = ketch.resume(id)

    override fun retryDownload(id: Int) = ketch.retry(id)

    override fun pauseAllDownloads() = ketch.pauseAll()

    override fun deleteDownload(id: Int) = ketch.clearDb(id, deleteFile = true)

    override fun deleteDownloads(ids: List<Int>) {
        ids.forEach { ketch.clearDb(it, deleteFile = true) }
    }

    override fun deleteAllDownloads() = ketch.clearAllDb(deleteFile = true)

    override suspend fun refreshDownloadDatabase() {
        val existingNames = getMeverFiles(meverFolder)
            .map { it.name.lowercase() }
            .toSet()
        ketch.observeDownloads().take(1).collect { downloads ->
            downloads
                .filter {
                    it.status == SUCCESS && existingNames.contains(it.fileName.lowercase()).not()
                }
                .forEach { ketch.clearDb(it.id) }
        }
    }
}