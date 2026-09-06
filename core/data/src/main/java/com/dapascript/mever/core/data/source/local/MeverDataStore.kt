package com.dapascript.mever.core.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "mever_data_store")

class MeverDataStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val dataStore = context.dataStore

    suspend fun <T : Any> saveValue(keyName: String, value: T) {
        val key = getPrefKey(keyName, value)
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun clearValue(keyName: String) {
        val key = getPrefKey(keyName, "")
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    fun <T : Any> getValue(keyName: String, defaultValue: T): Flow<T> {
        val key = getPrefKey(keyName, defaultValue)
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getPrefKey(key: String, value: T): Preferences.Key<T> {
        return when (value) {
            is String -> stringPreferencesKey(key)
            is Boolean -> booleanPreferencesKey(key)
            is Int -> intPreferencesKey(key)
            is Long -> longPreferencesKey(key)
            is Float -> floatPreferencesKey(key)
            is Double -> doublePreferencesKey(key)
            else -> throw IllegalArgumentException("Type ${value::class.java.simpleName} is not supported in DataStore")
        } as Preferences.Key<T>
    }

    companion object {
        const val KEY_VERSION = "version"
        const val KEY_IS_IMAGE_AI_ENABLED = "is_image_ai_enabled"
        const val KEY_IS_GO_IMG_ENABLED = "is_go_img_enabled"
        const val KEY_IS_ONBOARDED = "is_onboarded"
        const val KEY_SHOW_SUPPORTED_PLATFORM = "show_supported_platform"
        const val KEY_RESOLUTIONS = "youtube_resolutions"
        const val KEY_THEME = "theme"
        const val KEY_CLICK_COUNT = "click_count"
        const val KEY_ADS_THRESHOLD = "ads_threshold"
        const val KEY_LINK_CONTENT = "link_content"
        const val KEY_PIP = "pip_enabled"
        const val KEY_IS_FIRST_CHANGE = "is_first_change_language"

        const val MIN_ADS_THRESHOLD = 2
        const val MAX_ADS_THRESHOLD = 3
    }
}