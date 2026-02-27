package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.LanguageManager
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for SettingLanguageViewModel logic.
 *
 * NOTE: The ViewModel cannot be directly instantiated in JVM unit tests because
 * it calls SavedStateHandle.toRoute<SettingLanguageRoute>(typeMap) at construction time,
 * which requires the Navigation backstack (Android-only). Language-related logic
 * that is pure (no Android dependencies) is tested here.
 */
class SettingLanguageViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `LanguageManager appLanguages returns non-empty list`() {
        val languages = LanguageManager.appLanguages()
        assertFalse("Language list should not be empty", languages.isEmpty())
    }

    @Test
    fun `each language entry has a non-blank code`() {
        LanguageManager.appLanguages().forEach { (code, _) ->
            assertFalse("Language code '$code' should not be blank", code.isBlank())
        }
    }

    @Test
    fun `each language entry has a non-blank name`() {
        LanguageManager.appLanguages().forEach { (_, name) ->
            assertFalse("Language name '$name' should not be blank", name.isBlank())
        }
    }
}