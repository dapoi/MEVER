package com.dapascript.mever.feature.wa.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class WaStatusViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var context: Context

    private lateinit var viewModel: WaStatusViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = WaStatusViewModel(context)
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