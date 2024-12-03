package com.dapascript.mever.core.common.navigation.graph

import com.dapascript.mever.core.common.navigation.base.BaseNavGraph
import kotlinx.serialization.Serializable

@Serializable
object GalleryNavGraphRoute

abstract class GalleryNavGraph : BaseNavGraph() {
    abstract fun getGalleryLandingRoute(): Any
}