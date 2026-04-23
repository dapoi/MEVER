package com.dapascript.mever.core.navigation.base

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface BaseNavGraph {
    fun createGraph(
        navController: NavController,
        navGraphBuilder: NavGraphBuilder
    )
}