package com.dapascript.mever.feature.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.ketch.Ketch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
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
import org.mockito.kotlin.verifyNoInteractions

@OptIn(ExperimentalCoroutinesApi::class)
class HomeImageGeneratorResultViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock lateinit var ketch: Ketch
    @Mock lateinit var repository: MeverRepository

    private lateinit var viewModel: HomeImageGeneratorResultViewModel

    private val fakeImageAi = ImageAiEntity(
        imagesUrl = listOf("https://ai-img1.jpg", "https://ai-img2.jpg")
    )

    /** Helper to reflectively get the backing _aiResponseState flow */
    @Suppress("UNCHECKED_CAST")
    private fun backingFlow(): MutableStateFlow<UiState<ImageAiEntity>> {
        val field = HomeImageGeneratorResultViewModel::class.java
            .getDeclaredField("_aiResponseState")
        field.isAccessible = true
        return field.get(viewModel) as MutableStateFlow<UiState<ImageAiEntity>>
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        // Empty SavedStateHandle - args is lazy and only accessed when getImageAiGenerator() is called.
        // We test state directly via the backing flow, avoiding the toRoute() navigation internals.
        viewModel = HomeImageGeneratorResultViewModel(SavedStateHandle(), ketch, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `aiResponseState is StateInitial by default`() {
        assertTrue(viewModel.aiResponseState.value is UiState.StateInitial)
    }

    @Test
    fun `aiResponseState emits StateLoading when set via backing flow`() = testScope.runTest {
        val states = mutableListOf<UiState<ImageAiEntity>>()
        val job = launch { viewModel.aiResponseState.collect { states.add(it) } }
        backingFlow().value = UiState.StateLoading
        advanceUntilIdle()
        job.cancel()
        assertTrue(states.any { it is UiState.StateLoading })
    }

    @Test
    fun `aiResponseState emits StateSuccess when set via backing flow`() = testScope.runTest {
        val states = mutableListOf<UiState<ImageAiEntity>>()
        val job = launch { viewModel.aiResponseState.collect { states.add(it) } }
        backingFlow().value = UiState.StateSuccess(fakeImageAi)
        advanceUntilIdle()
        job.cancel()
        assertTrue(states.any { it is UiState.StateSuccess })
        assertEquals(fakeImageAi, (states.first { it is UiState.StateSuccess } as UiState.StateSuccess).data)
    }

    @Test
    fun `aiResponseState emits StateFailed when set via backing flow`() = testScope.runTest {
        val states = mutableListOf<UiState<ImageAiEntity>>()
        val job = launch { viewModel.aiResponseState.collect { states.add(it) } }
        backingFlow().value = UiState.StateFailed("AI error")
        advanceUntilIdle()
        job.cancel()
        assertTrue(states.any { it is UiState.StateFailed })
        assertEquals("AI error", (states.first { it is UiState.StateFailed } as UiState.StateFailed).message)
    }

    @Test
    fun `startDownload does nothing when url is empty`() {
        viewModel.startDownload("")
        verifyNoInteractions(ketch)
    }

    @Test
    fun `startDownload does nothing when url is blank whitespace`() {
        viewModel.startDownload("   ")
        verifyNoInteractions(ketch)
    }

    @Test
    fun `aiResponseState starts as StateInitial and is distinct from StateLoading`() {
        assertTrue(viewModel.aiResponseState.value !is UiState.StateLoading)
    }
}

