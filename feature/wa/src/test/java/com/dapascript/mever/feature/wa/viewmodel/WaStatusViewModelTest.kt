package com.dapascript.mever.feature.wa.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.repository.SubscriptionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class WaStatusViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repository: MeverRepository

    @Mock
    lateinit var subscriptionRepository: SubscriptionRepository

    private lateinit var viewModel: WaStatusViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(subscriptionRepository.isProActive).thenReturn(MutableStateFlow(false))
        viewModel = WaStatusViewModel(repository, subscriptionRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `waStatuses is null before first fetch`() {
        assertNull(viewModel.waStatuses.value)
    }

    @Test
    fun `onFetchFinished sets empty list when state is null`() = runTest {
        viewModel.onFetchFinished()
        advanceUntilIdle()
        assertTrue(viewModel.waStatuses.value?.isEmpty() == true)
    }

    @Test
    fun `onFetchFinished does not clear already fetched statuses`() = runTest {
        // Simulate an already-non-null state
        viewModel.onFetchFinished()
        advanceUntilIdle()
        val existing = viewModel.waStatuses.value

        // Calling onFetchFinished again must not reset the non-null state
        viewModel.onFetchFinished()
        advanceUntilIdle()
        assertTrue(viewModel.waStatuses.value == existing)
    }
}