package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SettingAboutAppViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    lateinit var dataStore: MeverDataStore

    private lateinit var viewModel: SettingAboutAppViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.System))
        viewModel = SettingAboutAppViewModel(dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `themeType collects System by default`() = testScope.runTest {
        val values = mutableListOf<ThemeType>()
        val job = launch { viewModel.themeType.collect { values.add(it) } }
        advanceUntilIdle()
        job.cancel()
        assertTrue(values.contains(ThemeType.System))
    }

    @Test
    fun `themeType collects Light when dataStore returns Light`() = testScope.runTest {
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.Light))
        val vm = SettingAboutAppViewModel(dataStore)
        val values = mutableListOf<ThemeType>()
        val job = launch { vm.themeType.collect { values.add(it) } }
        advanceUntilIdle()
        job.cancel()
        assertTrue(values.contains(ThemeType.Light))
    }

    @Test
    fun `themeType collects Dark when dataStore returns Dark`() = testScope.runTest {
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.Dark))
        val vm = SettingAboutAppViewModel(dataStore)
        val values = mutableListOf<ThemeType>()
        val job = launch { vm.themeType.collect { values.add(it) } }
        advanceUntilIdle()
        job.cancel()
        assertTrue(values.contains(ThemeType.Dark))
    }
}