package com.dapascript.mever.core.data.repository.base

import android.content.Context
import androidx.work.WorkManager
import com.dapascript.mever.core.data.util.MoshiHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class BaseRepositoryArgs @Inject constructor(
    @ApplicationContext val context: Context,
    val workManager: WorkManager,
    val moshiHelper: MoshiHelper
)