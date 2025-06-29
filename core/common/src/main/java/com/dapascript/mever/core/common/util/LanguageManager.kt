package com.dapascript.mever.core.common.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.LocaleList
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
}