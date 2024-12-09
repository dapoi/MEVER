package com.dapascript.mever.feature.gallery.navigation.route

import kotlinx.serialization.Serializable

@Serializable
object GalleryLandingRoute

@Serializable
data class GalleryContentViewerRoute(val sourceFile: String, val fileName: String)