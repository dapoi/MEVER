package com.dapascript.mever.core.common.ui.attr

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.ui.theme.MeverCreamSemiPink
import com.dapascript.mever.core.common.ui.theme.MeverLightBlue
import com.dapascript.mever.core.common.ui.theme.MeverLightGreen
import com.dapascript.mever.core.common.ui.theme.MeverLightPurple
import com.dapascript.mever.core.common.ui.theme.MeverPink
import com.dapascript.mever.core.common.ui.theme.MeverTransparent
import com.dapascript.mever.core.common.util.Constant.PlatformName.FACEBOOK
import com.dapascript.mever.core.common.util.Constant.PlatformName.INSTAGRAM
import com.dapascript.mever.core.common.util.Constant.PlatformName.TIKTOK
import com.dapascript.mever.core.common.util.Constant.PlatformName.TWITTER
import com.dapascript.mever.core.common.util.Constant.PlatformName.YOUTUBE
import com.ketch.Status

object MeverCardAttr {
    data class MeverCardArgs(
        val source: String,
        val tag: String,
        val fileName: String,
        val status: Status,
        val progress: Int,
        val total: Long,
        val path: String,
        val icon: Int? = null,
        val iconBackgroundColor: Color? = null,
        val iconSize: Dp? = null,
        val iconPadding: Dp? = null,
        val urlThumbnail: String? = null
    )
}