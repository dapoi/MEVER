package com.dapascript.mever.core.navigation.base

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.navigation.helper.Navigator

interface BaseNavGraph {
    fun EntryProviderScope<NavKey>.createGraph(navigator: Navigator)
}