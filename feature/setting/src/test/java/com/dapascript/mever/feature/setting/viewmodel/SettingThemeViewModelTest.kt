package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SettingThemeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    lateinit var dataStore: MeverDataStore

    // Provide savedStateHandle with the SettingThemeRoute args
    private val savedStateHandle = SavedStateHandle(
        mapOf("themeType" to ThemeType.System)
    )

    private lateinit var viewModel: SettingThemeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.System))
        viewModel = SettingThemeViewModel(dataStore, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setThemeType Light calls dataStore saveTheme with Light`() = testScope.runTest {
        viewModel.setThemeType(ThemeType.Light)
        advanceUntilIdle()
        verify(dataStore).saveTheme(ThemeType.Light)
    }

    @Test
    fun `setThemeType Dark calls dataStore saveTheme with Dark`() = testScope.runTest {
        viewModel.setThemeType(ThemeType.Dark)
        advanceUntilIdle()
        verify(dataStore).saveTheme(ThemeType.Dark)
    }

    @Test
    fun `setThemeType System calls dataStore saveTheme with System`() = testScope.runTest {
        viewModel.setThemeType(ThemeType.System)
        advanceUntilIdle()
        verify(dataStore).saveTheme(ThemeType.System)
    }

    @Test
    fun `titleHeight default is 0`() {
        assertEquals(0, viewModel.titleHeight)
    }
}