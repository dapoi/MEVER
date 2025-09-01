package com.dapascript.mever.feature.gallery.screen.attr

import android.content.Context
import com.dapascript.mever.core.common.R

object GalleryLandingScreenAttr {

    enum class GalleryActionMenu(val resId: Int) {
        SELECT_ALL(R.string.select_all),
        SELECT_FILES(R.string.select_files),
        MORE(R.string.more),
        DELETE_ALL(R.string.delete_all),
        DELETE_SELECTED(R.string.delete_button),
        SHARE_SELECTED(R.string.share),
        PAUSE_ALL(R.string.pause_all),
        RESUME_ALL(R.string.resume_all),
        HIDE_FILTER(R.string.hide_filter),
        SHOW_FILTER(R.string.show_filter),
        SORT_LATEST(R.string.sort_latest),
        SORT_OLDEST(R.string.sort_oldest);

        fun getText(context: Context) = context.getString(resId)
    }
}