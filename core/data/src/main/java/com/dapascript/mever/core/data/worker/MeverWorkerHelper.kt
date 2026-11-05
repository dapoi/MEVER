package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dapascript.mever.core.common.util.worker.WorkerConstant.ACTION_DOWNLOAD
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_ACTION
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_IS_FROM_NOTIF
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_URL

object MeverWorkerHelper {

    fun enqueueDownloadWorker(context: Context, url: String) {
        val inputData = Data.Builder()
            .putString(KEY_ACTION, ACTION_DOWNLOAD)
            .putString(KEY_URL, url)
            .putBoolean(KEY_IS_FROM_NOTIF, true)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<MeverWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
