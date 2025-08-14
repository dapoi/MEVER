package com.dapascript.mever.core.common.ui.attr

import android.content.Context
import com.dapascript.mever.core.common.R

object MeverContentViewerAttr {
    enum class ContentViewerActionMenu(val resId: Int) {
        DELETE(R.string.delete_button),
        SHARE(R.string.share);

        fun getText(context: Context) = context.getString(resId)
    }
}