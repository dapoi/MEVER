package com.dapascript.mever.core.common.navigation.graph

import com.dapascript.mever.core.common.navigation.base.BaseNavGraph

abstract class GalleryNavGraph : BaseNavGraph() {
    abstract fun getGalleryLandingRoute(): Any
    abstract fun getGalleryContentDetailRoute(
        id: Int,
        sourceFile: String,
        fileName: String
    ): Any
}