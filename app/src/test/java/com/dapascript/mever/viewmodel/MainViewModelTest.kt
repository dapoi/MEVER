package com.dapascript.mever.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.deeplink.DeeplinkManager
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
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

    @Mock
    lateinit var subscriptionRepository: SubscriptionRepository

    private val isProActiveFlow = MutableStateFlow(false)
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(subscriptionRepository.isProActive).thenReturn(isProActiveFlow)
        whenever(repository.getPreference(MeverDataStore.KEY_THEME, ThemeType.System.name))
            .thenReturn(flowOf(ThemeType.System.name))
        whenever(repository.getPreference(MeverDataStore.KEY_THEME_PALETTE, com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name))
            .thenReturn(flowOf(com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name))
        whenever(repository.getPreference(MeverDataStore.KEY_APP_ICON, com.dapascript.mever.core.common.util.AppIconType.Default.name))
            .thenReturn(flowOf(com.dapascript.mever.core.common.util.AppIconType.Default.name))
        viewModel = MainViewModel(repository, deeplinkManager, subscriptionRepository)
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
        val vm = MainViewModel(repository, deeplinkManager, subscriptionRepository)
        val collected = mutableListOf<ThemeType>()
        val job = launch { vm.themeType.collect { collected.add(it) } }
        advanceUntilIdle()
        assertTrue(collected.contains(ThemeType.Dark))
        job.cancel()
    }

    @Test
    fun `resetInteractionTime calls repository savePreference with zero`() = testScope.runTest {
        viewModel.resetInteractionTime()
        advanceUntilIdle()
        org.mockito.kotlin.verify(repository).savePreference(MeverDataStore.KEY_LAST_INTERACTION_TIME, 0L)
    }

    @Test
    fun `checkIdleTimeout triggers onTimeout when last interaction was more than 3 minutes ago`() = testScope.runTest {
        val fourMinutesAgo = System.currentTimeMillis() - (4 * 60 * 1000L)
        whenever(repository.getPreference(MeverDataStore.KEY_LAST_INTERACTION_TIME, 0L))
            .thenReturn(flowOf(fourMinutesAgo))

        var timeoutCalled = false
        viewModel.checkIdleTimeout { timeoutCalled = true }
        advanceUntilIdle()

        assertTrue(timeoutCalled)
    }

    @Test
    fun `checkIdleTimeout does not trigger onTimeout when last interaction was less than 3 minutes ago`() = testScope.runTest {
        val oneMinuteAgo = System.currentTimeMillis() - (1 * 60 * 1000L)
        whenever(repository.getPreference(MeverDataStore.KEY_LAST_INTERACTION_TIME, 0L))
            .thenReturn(flowOf(oneMinuteAgo))

        var timeoutCalled = false
        viewModel.checkIdleTimeout { timeoutCalled = true }
        advanceUntilIdle()

        assertFalse(timeoutCalled)
    }

    @Test
    fun `when user is free and current theme palette is Doodle, reverts to Default`() = testScope.runTest {
        whenever(repository.getPreference(MeverDataStore.KEY_THEME_PALETTE, com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name))
            .thenReturn(flowOf(com.dapascript.mever.core.common.ui.theme.ThemePalette.Doodle.name))
        whenever(repository.getPreference(MeverDataStore.KEY_APP_ICON, com.dapascript.mever.core.common.util.AppIconType.Default.name))
            .thenReturn(flowOf(com.dapascript.mever.core.common.util.AppIconType.Default.name))

        MainViewModel(repository, deeplinkManager, subscriptionRepository)
        advanceUntilIdle()

        verify(repository).savePreference(
            MeverDataStore.KEY_THEME_PALETTE,
            com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name
        )
    }

    @Test
    fun `when user is free and current app icon is Doodle, reverts to Default`() = testScope.runTest {
        whenever(repository.getPreference(MeverDataStore.KEY_THEME_PALETTE, com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name))
            .thenReturn(flowOf(com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name))
        whenever(repository.getPreference(MeverDataStore.KEY_APP_ICON, com.dapascript.mever.core.common.util.AppIconType.Default.name))
            .thenReturn(flowOf(com.dapascript.mever.core.common.util.AppIconType.Doodle.name))

        MainViewModel(repository, deeplinkManager, subscriptionRepository)
        advanceUntilIdle()

        verify(repository).savePreference(
            MeverDataStore.KEY_APP_ICON,
            com.dapascript.mever.core.common.util.AppIconType.Default.name
        )
    }

    @Test
    fun `when user is pro and current theme is Doodle, does not revert`() = testScope.runTest {
        val proFlow = MutableStateFlow(true)
        whenever(subscriptionRepository.isProActive).thenReturn(proFlow)
        whenever(repository.getPreference(MeverDataStore.KEY_THEME_PALETTE, com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name))
            .thenReturn(flowOf(com.dapascript.mever.core.common.ui.theme.ThemePalette.Doodle.name))

        MainViewModel(repository, deeplinkManager, subscriptionRepository)
        advanceUntilIdle()

        verify(repository, never()).savePreference(
            MeverDataStore.KEY_THEME_PALETTE,
            com.dapascript.mever.core.common.ui.theme.ThemePalette.CLASSIC.name
        )
    }
}