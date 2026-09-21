package com.dapascript.mever.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.deeplink.DeeplinkManager
import com.dapascript.mever.core.data.repository.MeverRepository
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
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    lateinit var repository: MeverRepository

    @Mock
    lateinit var deeplinkManager: DeeplinkManager

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(repository.getPreference(MeverDataStore.KEY_THEME, ThemeType.System.name))
            .thenReturn(flowOf(ThemeType.System.name))
        viewModel = MainViewModel(repository, deeplinkManager)
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
    fun `themeType emits Dark when repository returns Dark`() = testScope.runTest {
        whenever(repository.getPreference(MeverDataStore.KEY_THEME, ThemeType.System.name))
            .thenReturn(flowOf(ThemeType.Dark.name))
        whenever(deeplinkManager.deeplinkEvent).thenReturn(flowOf(null))
        val vm = MainViewModel(repository, deeplinkManager)
        val collected = mutableListOf<ThemeType>()
        val job = launch { vm.themeType.collect { collected.add(it) } }
        advanceUntilIdle()
        assertTrue(collected.contains(ThemeType.Dark))
        job.cancel()
    }
}