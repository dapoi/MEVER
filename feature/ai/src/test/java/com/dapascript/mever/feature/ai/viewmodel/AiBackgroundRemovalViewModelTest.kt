package com.dapascript.mever.feature.ai.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.BackgroundRemovalProcessor
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.feature.ai.screen.attr.AiBackgroundRemovalAttr.BgRemovalType.QuickColor
import com.dapascript.mever.feature.ai.screen.attr.AiBackgroundRemovalAttr.BgRemovalType.TransparentImage
import com.ketch.Ketch
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AiBackgroundRemovalViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var processor: BackgroundRemovalProcessor

    @Mock
    lateinit var dataStore: MeverDataStore

    @Mock
    lateinit var repository: MeverRepository

    @Mock
    lateinit var ketch: Ketch

    private lateinit var viewModel: AiBackgroundRemovalViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        whenever(dataStore.clickCount).thenReturn(flowOf(2))
        whenever(dataStore.adsThreshold).thenReturn(flowOf(5))

        viewModel = AiBackgroundRemovalViewModel(context, processor, dataStore, repository, ketch)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `states are StateInitial by default`() {
        assertTrue(viewModel.backgroundRemovalState.value is UiState.StateInitial)
        assertTrue(viewModel.saveImageState.value is UiState.StateInitial)
    }

    @Test
    fun `selectedBackground is TransparentImage by default`() {
        assertTrue(viewModel.selectedBackground.value is TransparentImage)
    }

    @Test
    fun `getButtonClickCount collects value from dataStore`() = runTest {
        val values = mutableListOf<Int>()
        val job = launch { viewModel.getButtonClickCount.collect { values.add(it) } }
        advanceUntilIdle()
        assertTrue(values.contains(2))
        job.cancel()
    }

    @Test
    fun `adsThreshold collects value from dataStore`() = runTest {
        val values = mutableListOf<Int>()
        val job = launch { viewModel.adsThreshold.collect { values.add(it) } }
        advanceUntilIdle()
        assertTrue(values.contains(5))
        job.cancel()
    }

    @Test
    fun `selectBackground updates selected background type`() {
        viewModel.selectBackground(QuickColor(0xFF000000.toInt()))
        assertTrue(viewModel.selectedBackground.value is QuickColor)
    }

    @Test
    fun `reset restores default states and selection`() = runTest {
        viewModel.selectBackground(QuickColor(0xFF000000.toInt()))
        viewModel.reset()
        advanceUntilIdle()
        assertTrue(viewModel.selectedBackground.value is TransparentImage)
        assertTrue(viewModel.backgroundRemovalState.value is UiState.StateInitial)
        assertTrue(viewModel.saveImageState.value is UiState.StateInitial)
    }

    @Test
    fun `incrementClickCount calls dataStore incrementClickCount`() = runTest {
        viewModel.incrementClickCount()
        advanceUntilIdle()
        verify(dataStore).incrementClickCount()
    }
}