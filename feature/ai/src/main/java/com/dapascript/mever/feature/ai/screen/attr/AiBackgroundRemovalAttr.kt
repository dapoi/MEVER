package com.dapascript.mever.feature.ai.screen.attr

import android.graphics.Bitmap
import android.net.Uri

object AiBackgroundRemovalAttr {
    data class SaveResult(
        val location: ImageLocation,
        val fileName: String
    ) {
        enum class ImageLocation {
            IN_APP, GALLERY
        }
    }

    sealed class BgRemovalType {
        object TransparentImage : BgRemovalType()
        object CustomColor : BgRemovalType()
        data class QuickColor(val color: Int) : BgRemovalType()
        data class CustomImage(val uri: Uri, val bitmap: Bitmap? = null) : BgRemovalType()
    }
}