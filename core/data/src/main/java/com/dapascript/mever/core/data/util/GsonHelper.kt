package com.dapascript.mever.core.data.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GsonHelper {
    val gson = Gson()

    inline fun <reified T> T.toJson(): String {
        return gson.toJson(this)
    }

    inline fun <reified T> String.fromJson(): T {
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson(this, type)
    }
}