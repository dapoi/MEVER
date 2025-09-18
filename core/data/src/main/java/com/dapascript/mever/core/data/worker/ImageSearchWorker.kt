package com.dapascript.mever.core.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_REQUEST_QUERY
import com.dapascript.mever.core.common.util.worker.WorkerConstant.KEY_RESPONSE_IMAGES
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.source.remote.ApiService
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.worker.base.BaseWorker
import com.squareup.moshi.Types
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.lang.reflect.Type

@HiltWorker
class ImageSearchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    moshiHelper: MoshiHelper,
    private val apiService: ApiService
) : BaseWorker<List<ContentEntity>>(context, workerParameters, moshiHelper) {

    override val outputSuccessKey: String = KEY_RESPONSE_IMAGES
    override val resultType: Type = Types.newParameterizedType(
        List::class.java, ContentEntity::class.java
    )

    override suspend fun doApiCall(): List<ContentEntity> {
        val query = inputData.getString(KEY_REQUEST_QUERY).orEmpty()
        return apiService.getImageSearch(query).mapToEntity() ?: emptyList()
    }
}