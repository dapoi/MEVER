package com.dapascript.mever.core.common.navigation

import com.dapascript.mever.core.common.navigation.base.BaseNavGraph
import javax.inject.Inject

class MeverNavGraphs @Inject constructor(
    private val navGraphs: List<BaseNavGraph>
) {
    fun getNavGraphs() = navGraphs
}