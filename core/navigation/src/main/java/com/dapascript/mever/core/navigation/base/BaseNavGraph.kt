package com.dapascript.mever.core.navigation.base

import androidx.navigation.NavGraphBuilder

open class BaseNavGraph {
    open fun createGraph(
        navigator: BaseNavigator,
        navGraphBuilder: NavGraphBuilder
    ) = Unit
}