package com.dapascript.mever.core.common.navigation.base

import androidx.navigation.NavGraphBuilder

open class BaseNavGraph {
    open fun buildGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) = Unit
}