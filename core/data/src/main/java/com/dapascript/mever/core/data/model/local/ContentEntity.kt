package com.dapascript.mever.core.data.model.local

data class ContentEntity(
    val url: String,
    val status: Boolean,
    val id: String = "",
    val previewUrl: String = "",
    val fileName: String = "",
    val quality: String = "",
    val thumbnail: String = "",
    val type: String = "",
    val message: String = ""
)