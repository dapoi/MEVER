package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_APP_CONFIG
import com.dapascript.mever.core.data.model.local.AppConfigEntity
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.worker.base.BaseWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.reflect.Type

@HiltWorker
class AppConfigWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    moshiHelper: MoshiHelper,
    private val apiService: ApiService
) : BaseWorker<AppConfigEntity>(context, workerParameters, moshiHelper) {
    override val outputSuccessKey: String = KEY_RESPONSE_APP_CONFIG
    override val resultType: Type = AppConfigEntity::class.java

    override suspend fun doApiCall() = apiService.getAppConfig().mapToEntity()
}