package com.dapascript.mever.core.data.repository.base

import android.content.Context
import androidx.work.WorkManager
import com.dapascript.mever.core.data.util.MoshiHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BaseRepositoryArgs @Inject constructor(
    @param:ApplicationContext val context: Context,
    val workManager: WorkManager,
    val moshiHelper: MoshiHelper
)