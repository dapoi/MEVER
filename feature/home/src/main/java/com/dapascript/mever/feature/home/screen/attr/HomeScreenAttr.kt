package com.dapascript.mever.feature.home.screen.attr

import com.dapascript.mever.core.common.util.Constant.ScreenName.GALLERY
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING
import com.dapascript.mever.feature.home.R

object HomeScreenAttr {
    val listOfActionMenu = mapOf(
        GALLERY to R.drawable.ic_explore,
        SETTING to R.drawable.ic_setting
    )

    data class DownloaderArgs(
        val url: String,
        val quality: String
    )
}