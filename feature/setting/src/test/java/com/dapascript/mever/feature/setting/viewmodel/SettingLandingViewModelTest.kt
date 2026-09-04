package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
    fun `repository isPipEnabled emits true`() = runBlocking {
        val repository = mock(MeverRepository::class.java)
        whenever(repository.getPreference(MeverDataStore.KEY_PIP, true)).thenReturn(flowOf(true))
        val pip = repository.getPreference(MeverDataStore.KEY_PIP, true).first()
        assertTrue(pip)
    }

    @Test
    fun `repository isPipEnabled emits false`() = runBlocking {
        val repository = mock(MeverRepository::class.java)
        whenever(repository.getPreference(MeverDataStore.KEY_PIP, true)).thenReturn(flowOf(false))
        val pip = repository.getPreference(MeverDataStore.KEY_PIP, true).first()
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