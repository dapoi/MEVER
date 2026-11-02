package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class SettingSubscriptionDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
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
    fun `isProActive and proWillRenew reflect repository flows`() = runTest {
        whenever(subscriptionRepository.isProActive).thenReturn(MutableStateFlow(true))
        whenever(subscriptionRepository.proWillRenew).thenReturn(MutableStateFlow(true))
        whenever(subscriptionRepository.proExpirationDate).thenReturn(MutableStateFlow(null))

        val viewModel = SettingSubscriptionDetailViewModel(subscriptionRepository)

        assertTrue(viewModel.isProActive.value)
        assertTrue(viewModel.proWillRenew.value)
    }

    @Test
    fun `expirationDateString formats date correctly`() = runTest {
        val testDate = Date(1750000000000L) // 2025-06-15T...
        val expectedDateString = SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(testDate)

        whenever(subscriptionRepository.isProActive).thenReturn(MutableStateFlow(true))
        whenever(subscriptionRepository.proWillRenew).thenReturn(MutableStateFlow(true))
        whenever(subscriptionRepository.proExpirationDate).thenReturn(MutableStateFlow(testDate))

        val viewModel = SettingSubscriptionDetailViewModel(subscriptionRepository)
        backgroundScope.launch { viewModel.expirationDateString.collect {} }
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(expectedDateString, viewModel.expirationDateString.value)
    }
}
