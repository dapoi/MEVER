package com.dapascript.mever.core.common.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.setApplicationLocales
import androidx.core.os.LocaleListCompat

object LanguageManager {
    fun changeLanguage(context: Context, languageCode: String) {
        if (SDK_INT >= TIRAMISU) {
            context.getSystemService(
                LocaleManager::class.java
            ).applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }

    fun getLanguageCode(context: Context): String {
        val locale = if (SDK_INT >= TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                ?.applicationLocales
                ?.get(0)
        } else {
            AppCompatDelegate.getApplicationLocales().get(0)
        }
        return locale?.language ?: "en"
    }

    fun appLanguages() = listOf(
        "English" to "en",
        "Bahasa Indonesia" to "id"
    )
}