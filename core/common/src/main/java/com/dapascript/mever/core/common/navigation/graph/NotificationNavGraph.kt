package com.dapascript.mever.core.common.navigation.graph

import com.dapascript.mever.core.common.navigation.base.BaseNavGraph
import kotlinx.serialization.Serializable

@Serializable
object NotificationNavGraphRoute

abstract class NotificationNavGraph : BaseNavGraph() {
    abstract fun getNotificationLandingRoute(): Any
}