package com.dapascript.mever.core.common.navigation.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : Any> NavGraphBuilder.composableScreen(
    noinline content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable<T>(
        enterTransition = {
            slideIntoContainer(towards = Start, animationSpec = tween(400))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(400))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(400))
        },
        popExitTransition = {
            slideOutOfContainer(towards = End, animationSpec = tween(400))
        },
        content = content
    )
}

inline fun <reified T : Parcelable> generateNavType(isNullableAllowed: Boolean = false) =
    object : NavType<T>(isNullableAllowed = isNullableAllowed) {
        override fun get(bundle: Bundle, key: String) =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, T::class.java)
            } else bundle.getParcelable(key)

        override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)

        override fun parseValue(value: String) = Json.decodeFromString<T>(value)

        override fun serializeAsValue(value: T) = Json.encodeToString(value)
    }

fun buildDeepLink(uri: String) = navDeepLink { uriPattern = uri }