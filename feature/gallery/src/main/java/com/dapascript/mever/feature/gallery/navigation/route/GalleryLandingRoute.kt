package com.dapascript.mever.feature.gallery.navigation.route

import kotlinx.serialization.Serializable

@Serializable
object GalleryLandingRoute

@Serializable
data class GalleryPlayerRoute(val sourceVideo: String, val fileName: String)