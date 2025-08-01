package com.dapascript.mever.core.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "mever_data_store")

class MeverDataStore @Inject constructor(context: Context) {

    private val dataStore = context.dataStore

    suspend fun saveVersion(version: String) {
        dataStore.edit { preferences ->
            preferences[KEY_VERSION] = version
        }
    }

    val getVersion = dataStore.data.map { preferences ->
        preferences[KEY_VERSION] ?: "1.0.0"
    }

    suspend fun setIsImageAiEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_IMAGE_AI_ENABLED] = isEnabled
        }
    }

    val isImageAiEnabled = dataStore.data.map { preferences ->
        preferences[KEY_IS_IMAGE_AI_ENABLED] ?: true
    }

    suspend fun saveYoutubeResolutions(resolutions: List<String>) {
        dataStore.edit { preferences ->
            preferences[KEY_RESOLUTIONS] = resolutions.joinToString(",")
        }
    }

    val getYoutubeResolutions = dataStore.data.map { preferences ->
        preferences[KEY_RESOLUTIONS]?.split(",") ?: listOf("360p", "480p", "720p", "1080p")
    }

    suspend fun setIsOnboarded(isOnboarded: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_ONBOARDED] = isOnboarded
        }
    }

    val isOnboarded = dataStore.data.map { preferences ->
        preferences[KEY_IS_ONBOARDED] ?: false
    }

    suspend fun saveLanguageCode(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[KEY_LANGUAGE] = languageCode
        }
    }

    val getLanguageCode = dataStore.data.map { preferences ->
        preferences[KEY_LANGUAGE] ?: "en"
    }

    suspend fun saveTheme(mode: ThemeType) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME] = mode.name
        }
    }

    val getTheme = dataStore.data.map { preferences ->
        try {
            ThemeType.valueOf(preferences[KEY_THEME] ?: System.name)
        } catch (e: IllegalArgumentException) {
            System
        }
    }

    suspend fun incrementClickCount() {
        dataStore.edit { preferences ->
            val currentCount = preferences[KEY_CLICK_COUNT] ?: 1
            preferences[KEY_CLICK_COUNT] = if (currentCount == 4) 1 else currentCount + 1
        }
    }

    val clickCount = dataStore.data.map { preferences ->
        preferences[KEY_CLICK_COUNT] ?: 1
    }

    companion object {
        private val KEY_VERSION = stringPreferencesKey("version")
        private val KEY_IS_IMAGE_AI_ENABLED = booleanPreferencesKey("is_image_ai_enabled")
        private val KEY_RESOLUTIONS = stringPreferencesKey("youtube_resolutions")
        private val KEY_IS_ONBOARDED = booleanPreferencesKey("is_onboarded")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_CLICK_COUNT = intPreferencesKey("click_count")
    }
}