package com.dapascript.mever.core.data.model.remote

import com.dapascript.mever.core.data.model.local.ContentEntity
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class TwitterDownloaderResponse(
    @SerializedName("media") val media: List<Any>
) {
    data class DataVideo(
        @SerializedName("quality") val quality: String? = null,
        @SerializedName("url") val url: String? = null
    )

    fun mapToEntity(): List<ContentEntity> {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                object : TypeToken<List<Any>>() {}.type,
                MediaDeserializer()
            )
            .create()
        val mediaList = gson.toJsonTree(media).asJsonArray
        return mediaList.map { element ->
            when {
                element.isJsonObject -> {
                    val dataVideo = gson.fromJson(element, DataVideo::class.java)
                    ContentEntity(
                        url = dataVideo.url.orEmpty(),
                        quality = dataVideo.quality.orEmpty(),
                        thumbnail = ""
                    )
                }

                element.isJsonPrimitive -> {
                    ContentEntity(
                        url = element.asString,
                        quality = "",
                        thumbnail = ""
                    )
                }

                else -> {
                    ContentEntity(
                        url = "",
                        quality = "",
                        thumbnail = ""
                    )
                }
            }
        }
    }
}

private class MediaDeserializer : JsonDeserializer<List<Any>> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<Any> {
        val mediaList = mutableListOf<Any>()
        if (json.isJsonArray) {
            json.asJsonArray.forEach { element ->
                mediaList.add(
                    if (element.isJsonObject) {
                        context.deserialize<TwitterDownloaderResponse.DataVideo>(
                            element,
                            TwitterDownloaderResponse.DataVideo::class.java
                        )
                    } else {
                        element.asString
                    }
                )
            }
        }
        return mediaList
    }
}