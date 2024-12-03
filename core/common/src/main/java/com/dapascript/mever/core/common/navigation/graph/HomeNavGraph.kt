package com.dapascript.mever.core.common.navigation.graph

import com.dapascript.mever.core.common.navigation.base.BaseNavGraph
import kotlinx.serialization.Serializable

@Serializable
object HomeNavGraphRoute

abstract class HomeNavGraph : BaseNavGraph() {
    abstract fun getHomeLandingRoute(): Any
}