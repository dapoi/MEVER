package com.dapascript.mever.core.common.navigation.base

import androidx.navigation.NavGraphBuilder

open class BaseNavGraph {
    open fun createGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) = Unit
}