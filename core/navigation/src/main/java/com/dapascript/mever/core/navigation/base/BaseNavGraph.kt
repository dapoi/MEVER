package com.dapascript.mever.core.navigation.base

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

abstract class BaseNavGraph {
    abstract fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    )
}