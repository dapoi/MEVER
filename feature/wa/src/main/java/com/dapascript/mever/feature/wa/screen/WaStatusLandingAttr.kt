package com.dapascript.mever.feature.wa.screen

import android.net.Uri
import androidx.annotation.StringRes
import com.dapascript.mever.core.common.R

object WaStatusLandingAttr {
    data class WaMediaModel(
        val uri: Uri,
        val name: String,
        val lastModified: Long,
        val waType: WaType,
        val isVideo: Boolean
    ) {
        enum class WaType(@param:StringRes val label: Int) {
            ALL(R.string.all),
            REGULAR(R.string.wa_regular),
            BUSINESS(R.string.wa_business)
        }
    }
}