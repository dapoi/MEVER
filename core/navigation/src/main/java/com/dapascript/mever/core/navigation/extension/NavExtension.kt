package com.dapascript.mever.core.navigation.extension

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
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
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

inline fun <reified T : Any> NavGraphBuilder.composableScreen(
    customArgs: KClass<*>? = null,
    deepLinks: List<NavDeepLink>? = null,
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable<T>(
        typeMap = customArgs?.let { mapOf(typeOf<T>() to customNavType<T>()) } ?: emptyMap(),
        deepLinks = deepLinks ?: emptyList(),
        enterTransition = {
            slideIntoContainer(towards = Start, animationSpec = tween(350))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(350))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(350))
        },
        popExitTransition = {
            slideOutOfContainer(towards = End, animationSpec = tween(350))
        },
        content = content
    )
}

inline fun <reified T : Any> customNavType(
    isNullableAllowed: Boolean = false
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) = if (SDK_INT >= TIRAMISU) {
        bundle.getParcelable(key, T::class.java)
    } else bundle.getParcelable(key)

    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value as Parcelable)

    override fun parseValue(value: String) = Json.decodeFromString<T>(value)

    override fun serializeAsValue(value: T) = Json.encodeToString(value)
}

fun buildDeepLink(uri: String) = navDeepLink { uriPattern = uri }