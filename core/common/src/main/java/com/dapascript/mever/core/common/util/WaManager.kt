package com.dapascript.mever.core.common.util

import android.net.Uri

object WaManager {
    enum class WaType(val label: String) {
        ALL("All"),
        REGULAR("WA Regular"),
        BUSINESS("WA Business")
    }

    data class WaMediaModel(
        val uri: Uri,
        val name: String,
        val lastModified: Long,
        val waType: WaType,
        val isVideo: Boolean
    )
}