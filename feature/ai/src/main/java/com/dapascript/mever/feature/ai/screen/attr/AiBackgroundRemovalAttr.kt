package com.dapascript.mever.feature.ai.screen.attr

import android.graphics.Bitmap
import android.net.Uri

internal object AiBackgroundRemovalAttr {
    internal data class SaveResult(
        val location: ImageLocation,
        val fileName: String
    ) {
        enum class ImageLocation {
            IN_APP, GALLERY
        }
    }

    sealed class BgRemovalType {
        internal object TransparentImage : BgRemovalType()
        internal data class CustomColor(val color: Int) : BgRemovalType()
        internal data class QuickColor(val color: Int) : BgRemovalType()
        internal data class CustomImage(val uri: Uri, val bitmap: Bitmap? = null) : BgRemovalType()
    }
}