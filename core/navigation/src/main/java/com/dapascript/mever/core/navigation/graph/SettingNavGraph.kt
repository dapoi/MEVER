package com.dapascript.mever.core.navigation.graph

import com.dapascript.mever.core.navigation.base.BaseNavGraph

abstract class SettingNavGraph : BaseNavGraph() {
    abstract fun getSettingLandingRoute(): Any
}