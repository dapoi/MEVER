package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class SettingThemeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    lateinit var repository: MeverRepository

    // Provide savedStateHandle with the SettingThemeRoute args
    private val savedStateHandle = SavedStateHandle(
        mapOf("themeType" to ThemeType.System)
    )

    private lateinit var viewModel: SettingThemeViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingThemeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setThemeType Light calls repository savePreference with Light`() = testScope.runTest {
        viewModel.setThemeType(ThemeType.Light)
        advanceUntilIdle()
        verify(repository).savePreference(MeverDataStore.KEY_THEME, ThemeType.Light.name)
    }

    @Test
    fun `setThemeType Dark calls repository savePreference with Dark`() = testScope.runTest {
        viewModel.setThemeType(ThemeType.Dark)
        advanceUntilIdle()
        verify(repository).savePreference(MeverDataStore.KEY_THEME, ThemeType.Dark.name)
    }

    @Test
    fun `setThemeType System calls repository savePreference with System`() = testScope.runTest {
        viewModel.setThemeType(ThemeType.System)
        advanceUntilIdle()
        verify(repository).savePreference(MeverDataStore.KEY_THEME, ThemeType.System.name)
    }

}