package com.dapascript.mever.core.data.model.local

import android.net.Uri
import androidx.annotation.StringRes
import com.dapascript.mever.core.common.R

data class WaStatusEntity(
    val uri: Uri,
    val name: String,
    val lastModified: Long,
    val waType: WaType,
    val isVideo: Boolean
)

enum class WaType(@StringRes val label: Int) {
    ALL(R.string.all),
    REGULAR(R.string.wa_regular),
    BUSINESS(R.string.wa_business)
}