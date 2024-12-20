package com.dapascript.mever.core.common.navigation.graph

import com.dapascript.mever.core.common.navigation.base.BaseNavGraph
import kotlinx.serialization.Serializable

@Serializable
object SettingNavGraphRoute

abstract class SettingNavGraph : BaseNavGraph() {
    abstract fun getSettingLandingRoute(): Any
}