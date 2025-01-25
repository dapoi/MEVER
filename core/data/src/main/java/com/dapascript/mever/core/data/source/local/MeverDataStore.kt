package com.dapascript.mever.core.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.common.ui.theme.ThemeType.System
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "mever_data_store")

class MeverDataStore @Inject constructor(context: Context) {

    private val dataStore = context.dataStore

    suspend fun setIsOnboarded(isOnboarded: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_ONBOARDED] = isOnboarded
        }
    }

    val isOnboarded = dataStore.data.map { preferences ->
        preferences[KEY_IS_ONBOARDED] ?: false
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

    companion object {
        private val KEY_IS_ONBOARDED = booleanPreferencesKey("is_onboarded")
        private val KEY_THEME = stringPreferencesKey("theme")
    }
}