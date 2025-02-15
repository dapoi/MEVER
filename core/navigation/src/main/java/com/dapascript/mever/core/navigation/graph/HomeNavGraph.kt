package com.dapascript.mever.core.navigation.graph

import com.dapascript.mever.core.navigation.base.BaseNavGraph

abstract class HomeNavGraph : BaseNavGraph() {
    abstract fun getHomeLandingRoute(): Any
}