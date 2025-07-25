package com.dapascript.mever.core.navigation.helper

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

inline fun <reified T : Any> NavGraphBuilder.composableScreen(
    customArgs: Map<KType, NavType<*>>? = null,
    deepLinks: List<NavDeepLink>? = null,
    enterTransition: EnterTransition? = null,
    exitTransition: ExitTransition? = null,
    popEnterTransition: EnterTransition? = null,
    popExitTransition: ExitTransition? = null,
    noinline content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable<T>(
        typeMap = customArgs ?: emptyMap(),
        deepLinks = deepLinks ?: emptyList(),
        enterTransition = { enterTransition ?: slideIntoContainer(Start, tween(350)) },
        exitTransition = { exitTransition ?: fadeOut(tween(350)) },
        popEnterTransition = { popEnterTransition ?: fadeIn(tween(350)) },
        popExitTransition = { popExitTransition ?: slideOutOfContainer(End, tween(350)) },
        content = content
    )
}

inline fun <reified T : Any> customNavType(
    isNullableAllowed: Boolean = false,
    json: Json = Json
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        bundle.getString(key)?.let<String, T>(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(value)

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}

inline fun <reified T : Any> generateCustomNavType() = typeOf<T>() to customNavType<T>()

@SuppressLint("RestrictedApi")
fun NavController.navigateTo(
    route: Any,
    popUpTo: KClass<*>? = null,
    inclusive: Boolean = false,
    launchSingleTop: Boolean = false
) {
    navigate(route) {
        popUpTo?.let {
            popUpTo(it) { this.inclusive = inclusive }
        }
        this.launchSingleTop = launchSingleTop
    }
}

fun NavController.navigateClearBackStack(route: Any) {
    navigate(route) {
        popUpTo(graph.id) {
            inclusive = false
            saveState = false
        }
        launchSingleTop = true
        restoreState = false
    }
}

inline fun <reified T> NavController.setPopBackStackWithCustomArgs(
    key: String,
    value: T
) {
    popBackStack()
    previousBackStackEntry?.savedStateHandle?.set(key, Json.encodeToString(value))
}

inline fun <reified T> NavController.getPopBackStackWithCustomArgs(
    key: String
): T? = try {
    currentBackStackEntry?.savedStateHandle?.run {
        val result = get<String>(key).orEmpty()
        remove<String>(key)
        Json.decodeFromString(result)
    }
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun buildDeepLink(uri: String) = navDeepLink { uriPattern = uri }