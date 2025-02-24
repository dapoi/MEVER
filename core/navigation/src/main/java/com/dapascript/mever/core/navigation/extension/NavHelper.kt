package com.dapascript.mever.core.navigation.extension

import android.net.Uri
import android.os.Bundle
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
import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T : Any> NavGraphBuilder.composableScreen(
    customArgs: Map<KType, NavType<*>>? = null,
    deepLinks: List<NavDeepLink>? = null,
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable<T>(
        typeMap = customArgs ?: emptyMap(),
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
    isNullableAllowed: Boolean = false,
    json: Json = Json
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) = bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}

inline fun <reified T : Any> generateCustomNavType() = typeOf<T>() to customNavType<T>()

fun buildDeepLink(uri: String) = navDeepLink { uriPattern = uri }