package com.dapascript.mever.viewmodel

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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    lateinit var dataStore: MeverDataStore

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.System))
        viewModel = MainViewModel(dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `themeType initial value is System`() = testScope.runTest {
        // Collect to activate WhileSubscribed
        val collected = mutableListOf<ThemeType>()
        val job = launch { viewModel.themeType.collect { collected.add(it) } }
        advanceUntilIdle()
        assertTrue(collected.contains(ThemeType.System))
        job.cancel()
    }

    @Test
    fun `themeType emits Dark when dataStore returns Dark`() = testScope.runTest {
        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.Dark))
        val vm = MainViewModel(dataStore)
        val collected = mutableListOf<ThemeType>()
        val job = launch { vm.themeType.collect { collected.add(it) } }
        advanceUntilIdle()
        assertTrue(collected.contains(ThemeType.Dark))
        job.cancel()
    }

    @Test
    fun `saveUrlIntent calls dataStore saveUrlIntent`() = testScope.runTest {
        val url = "https://example.com"
        viewModel.saveUrlIntent(url)
        advanceUntilIdle()
        verify(dataStore).saveUrlIntent(url)
    }

    @Test
    fun `navigationToHomeEvent emits after saveUrlIntent`() = testScope.runTest {
        val events = mutableListOf<Unit>()
        val job = launch { viewModel.navigationToHomeEvent.collect { events.add(it) } }
        viewModel.saveUrlIntent("https://example.com")
        advanceUntilIdle()
        assertTrue(events.isNotEmpty())
        job.cancel()
    }
}