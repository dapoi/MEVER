package com.dapascript.mever.core.common.navigation.graph

import com.dapascript.mever.core.common.navigation.base.BaseNavGraph

abstract class HomeNavGraph : BaseNavGraph() {
    abstract fun getHomeLandingRoute(): Any
}