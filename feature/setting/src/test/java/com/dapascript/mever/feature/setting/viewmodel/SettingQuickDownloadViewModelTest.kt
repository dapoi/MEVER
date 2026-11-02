package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_QUICK_NOTIF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SettingQuickDownloadViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val repository = mock(MeverRepository::class.java)
    private val subscriptionRepository = mock(SubscriptionRepository::class.java)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isQuickNotifEnabled reflects repository preference`() = runTest {
        whenever(repository.getPreference(KEY_QUICK_NOTIF, false)).thenReturn(flowOf(true))
        whenever(subscriptionRepository.isProActive).thenReturn(MutableStateFlow(true))

        val viewModel = SettingQuickDownloadViewModel(repository, subscriptionRepository)
        backgroundScope.launch { viewModel.isQuickNotifEnabled.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.isQuickNotifEnabled.value)
    }

    @Test
    fun `saveQuickNotifState calls repository savePreference`() = runTest {
        whenever(repository.getPreference(KEY_QUICK_NOTIF, false)).thenReturn(flowOf(false))
        whenever(subscriptionRepository.isProActive).thenReturn(MutableStateFlow(true))

        val viewModel = SettingQuickDownloadViewModel(repository, subscriptionRepository)
        viewModel.saveQuickNotifState(true)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).savePreference(KEY_QUICK_NOTIF, true)
    }

    @Test
    fun `isProActive reflects subscriptionRepository isProActive`() = runTest {
        whenever(repository.getPreference(KEY_QUICK_NOTIF, false)).thenReturn(flowOf(false))
        whenever(subscriptionRepository.isProActive).thenReturn(MutableStateFlow(true))

        val viewModel = SettingQuickDownloadViewModel(repository, subscriptionRepository)

        assertTrue(viewModel.isProActive.value)
    }
}
