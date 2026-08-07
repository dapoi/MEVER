package com.dapascript.mever.feature.gallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryContentDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var dataStore: MeverDataStore

    private lateinit var viewModel: GalleryContentDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        whenever(dataStore.isPipEnabled).thenReturn(flowOf(true))
        whenever(dataStore.clickCount).thenReturn(flowOf(1))
        whenever(dataStore.adsThreshold).thenReturn(flowOf(3))

        viewModel = GalleryContentDetailViewModel(dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isPipEnabled initial value is true`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.isPipEnabled.value)
    }

    @Test
    fun `getButtonClickCount collects value from dataStore`() = runTest {
        val values = mutableListOf<Int>()
        val job = launch { viewModel.getButtonClickCount.collect { values.add(it) } }
        advanceUntilIdle()
        assertTrue(values.contains(1))
        job.cancel()
    }

    @Test
    fun `adsThreshold initial value is 3`() = runTest {
        advanceUntilIdle()
        assertEquals(3, viewModel.adsThreshold.value)
    }

    @Test
    fun `incrementClickCount calls dataStore incrementClickCount`() = runTest {
        viewModel.incrementClickCount()
        advanceUntilIdle()
        verify(dataStore).incrementClickCount()
    }
}