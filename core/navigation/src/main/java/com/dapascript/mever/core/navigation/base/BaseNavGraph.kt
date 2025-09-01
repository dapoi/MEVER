package com.dapascript.mever.core.navigation.base

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.dapascript.mever.core.common.util.DeviceType

interface BaseNavGraph {
    fun createGraph(
        navController: NavController,
        deviceType: DeviceType,
        navGraphBuilder: NavGraphBuilder
    )
}