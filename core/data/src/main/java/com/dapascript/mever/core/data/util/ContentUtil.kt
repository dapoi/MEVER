package com.dapascript.mever.core.data.util

import com.dapascript.mever.core.common.util.PlatformType.CAPCUT
import com.dapascript.mever.core.common.util.PlatformType.DOUYIN
import com.dapascript.mever.core.common.util.PlatformType.FACEBOOK
import com.dapascript.mever.core.common.util.PlatformType.INSTAGRAM
import com.dapascript.mever.core.common.util.PlatformType.PINTEREST
import com.dapascript.mever.core.common.util.PlatformType.PIXIV
import com.dapascript.mever.core.common.util.PlatformType.TERABOX
import com.dapascript.mever.core.common.util.PlatformType.THREADS
import com.dapascript.mever.core.common.util.PlatformType.TIKTOK
import com.dapascript.mever.core.common.util.PlatformType.VIDEY
import com.dapascript.mever.core.common.util.PlatformType.X
import com.dapascript.mever.core.common.util.PlatformType.YOUTUBE
import com.dapascript.mever.core.common.util.getPlatformType
import com.dapascript.mever.core.data.model.local.ContentEntity

object ContentUtil {

    fun selectBestContents(
        contents: List<ContentEntity>,
        url: String,
    ): List<ContentEntity> {
        if (contents.isEmpty()) return emptyList()

        return when (getPlatformType(url)) {
            YOUTUBE -> {
                val yt480 = contents.firstOrNull { (it.quality).contains("480p", ignoreCase = true) }
                    ?: contents.firstOrNull { (it.quality).contains("720p", ignoreCase = true) }
                    ?: contents.firstOrNull { (it.quality).contains("360p", ignoreCase = true) }
                    ?: contents.first()
                listOf(yt480)
            }

            FACEBOOK -> {
                val fbHd = contents.firstOrNull { (it.quality).contains("hd", ignoreCase = true) }
                    ?: contents.first()
                listOf(fbHd)
            }

            TIKTOK, INSTAGRAM, THREADS, X, PINTEREST, PIXIV, TERABOX, CAPCUT, DOUYIN, VIDEY -> {
                contents
            }

            else -> contents
        }
    }
}
