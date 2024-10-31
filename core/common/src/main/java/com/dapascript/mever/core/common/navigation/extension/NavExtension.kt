package com.dapascript.mever.core.common.navigation.extension

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.navigation.navDeepLink
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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