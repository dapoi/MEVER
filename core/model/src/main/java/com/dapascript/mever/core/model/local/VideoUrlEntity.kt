package com.dapascript.mever.core.model.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoUrlEntity(
    val quality: String,
    val url: String
) : Parcelable