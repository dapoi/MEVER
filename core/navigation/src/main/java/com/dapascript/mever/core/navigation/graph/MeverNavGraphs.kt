package com.dapascript.mever.core.navigation.graph

import com.dapascript.mever.core.navigation.base.BaseNavGraph
import javax.inject.Inject

class MeverNavGraphs @Inject constructor(
    private val navGraphs: List<BaseNavGraph>
) {
    fun getNavGraphs() = navGraphs
}