package com.dapascript.mever.core.navigation.deeplink

import androidx.navigation3.runtime.NavKey
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.PATH_HOME
import com.dapascript.mever.core.navigation.route.HomeScreenRoute.HomeLandingRoute

object MeverDeeplinkRegistry {
    private val registry = mapOf<String, NavKey>(
        PATH_HOME to HomeLandingRoute
    )

    operator fun get(path: String?): NavKey = registry[path] ?: HomeLandingRoute
}