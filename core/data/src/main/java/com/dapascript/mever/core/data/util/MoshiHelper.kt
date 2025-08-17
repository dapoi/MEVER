package com.dapascript.mever.core.data.util

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

class MoshiHelper @Inject constructor(val moshi: Moshi) {

    fun <T> toJson(type: Type, data: T): String? {
        return try {
            val adapter: JsonAdapter<T> = moshi.adapter(type)
            adapter.toJson(data)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    inline fun <reified T> fromJson(json: String): T? {
        return try {
            moshi.adapter<T>(resolveType<T>()).fromJson(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    inline fun <reified T> resolveType(): Type {
        val superType = object : TypeToken<T>() {}.type
        return superType
    }

    abstract class TypeToken<T> {
        val type: Type = (this::class.java.genericSuperclass as ParameterizedType).actualTypeArguments[0]
    }
}