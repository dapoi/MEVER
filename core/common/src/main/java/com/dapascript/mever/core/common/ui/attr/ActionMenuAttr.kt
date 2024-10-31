package com.dapascript.mever.core.common.ui.attr

import com.dapascript.mever.core.common.util.Constant.ScreenName.EXPLORE
import com.dapascript.mever.core.common.util.Constant.ScreenName.NOTIFICATION
import com.dapascript.mever.core.common.util.Constant.ScreenName.SETTING

object ActionMenuAttr {

    private const val CONTENT_DESCRIPTION_ACTION_NOTIFICATION = "Action Notification"
    private const val CONTENT_DESCRIPTION_ACTION_EXPLORE = "Action Explore"
    private const val CONTENT_DESCRIPTION_ACTION_SETTING = "Action Setting"

    fun getContentDescription(screenName: String) = mapOf(
        NOTIFICATION to CONTENT_DESCRIPTION_ACTION_NOTIFICATION,
        EXPLORE to CONTENT_DESCRIPTION_ACTION_EXPLORE,
        SETTING to CONTENT_DESCRIPTION_ACTION_SETTING
    ).getValue(screenName)

    data class ActionMenu(
        val resource: Int,
        val name: String,
        val isShowBadge: Boolean = false,
    )
}