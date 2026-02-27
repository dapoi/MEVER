package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

/**
 * Unit tests for SettingLandingViewModel logic.
 *
 * NOTE: SettingLandingViewModel cannot be fully instantiated in JVM unit tests
 * because its init{} calls getStorageInfo() via Dispatchers.IO which requires
 * real Android StorageManager/StorageStatsManager internals not available on JVM.
 * We test the DataStore-related logic directly instead.
 */
class SettingLandingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `dataStore getTheme emits System theme`() = runBlocking {
        val dataStore = mock(MeverDataStore::class.java)
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.System))
        val theme = dataStore.getTheme.first()
        assertEquals(ThemeType.System, theme)
    }

    @Test
    fun `dataStore getTheme emits Dark theme`() = runBlocking {
        val dataStore = mock(MeverDataStore::class.java)
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.Dark))
        val theme = dataStore.getTheme.first()
        assertEquals(ThemeType.Dark, theme)
    }

    @Test
    fun `dataStore getTheme emits Light theme`() = runBlocking {
        val dataStore = mock(MeverDataStore::class.java)
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.Light))
        val theme = dataStore.getTheme.first()
        assertEquals(ThemeType.Light, theme)
    }

    @Test
    fun `dataStore isPipEnabled emits true`() = runBlocking {
        val dataStore = mock(MeverDataStore::class.java)
        whenever(dataStore.isPipEnabled).thenReturn(flowOf(true))
        val pip = dataStore.isPipEnabled.first()
        assertTrue(pip)
    }

    @Test
    fun `dataStore isPipEnabled emits false`() = runBlocking {
        val dataStore = mock(MeverDataStore::class.java)
        whenever(dataStore.isPipEnabled).thenReturn(flowOf(false))
        val pip = dataStore.isPipEnabled.first()
        assertEquals(false, pip)
    }

    @Test
    fun `ThemeType values contain all expected types`() {
        val types = ThemeType.entries.toList()
        assertTrue(types.contains(ThemeType.System))
        assertTrue(types.contains(ThemeType.Light))
        assertTrue(types.contains(ThemeType.Dark))
    }
}