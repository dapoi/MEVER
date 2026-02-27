package com.dapascript.mever.feature.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.ketch.Ketch
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeLandingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock lateinit var dataStore: MeverDataStore
    @Mock lateinit var ketch: Ketch
    @Mock lateinit var repository: MeverRepository

    private lateinit var viewModel: HomeLandingViewModel

    private val fakeContents = listOf(
        ContentEntity(url = "https://video.mp4", status = true, fileName = "video.mp4")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(dataStore.isImageAiEnabled).thenReturn(flowOf(true))
        whenever(dataStore.isGoImgEnabled).thenReturn(flowOf(true))
        whenever(dataStore.getYoutubeVideoAndAudioQuality).thenReturn(flowOf(emptyList()))
        whenever(dataStore.clickCount).thenReturn(flowOf(1))
        whenever(dataStore.getUrlIntent).thenReturn(flowOf(""))
        whenever(ketch.observeDownloads()).thenReturn(flowOf(emptyList()))
        viewModel = HomeLandingViewModel(dataStore, ketch, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `downloaderResponseState is StateInitial by default`() {
        assertTrue(viewModel.downloaderResponseState.value is UiState.StateInitial)
    }

    @Test
    fun `getApiDownloader passes through StateSuccess`() = testScope.runTest {
        whenever(repository.getDownloader(any(), any())).thenReturn(
            flowOf(ApiState.Success(fakeContents))
        )
        // Collect all states emitted during the call
        val states = mutableListOf<UiState<List<ContentEntity>>>()
        val job = launch { viewModel.downloaderResponseState.collect { states.add(it) } }
        viewModel.getApiDownloader()
        advanceUntilIdle()
        job.cancel()
        // StateSuccess should have been emitted at some point (before onReset fires)
        assertTrue(states.any { it is UiState.StateSuccess })
        assertEquals(fakeContents, (states.first { it is UiState.StateSuccess } as UiState.StateSuccess).data)
    }

    @Test
    fun `getApiDownloader passes through StateFailed and sets errorMessage`() = testScope.runTest {
        whenever(repository.getDownloader(any(), any())).thenReturn(
            flowOf(ApiState.Error(Throwable("Server error")))
        )
        val states = mutableListOf<UiState<List<ContentEntity>>>()
        val job = launch { viewModel.downloaderResponseState.collect { states.add(it) } }
        viewModel.getApiDownloader()
        advanceUntilIdle()
        job.cancel()
        assertTrue(states.any { it is UiState.StateFailed })
        assertEquals("Server error", viewModel.errorMessage)
    }

    @Test
    fun `getApiDownloader emits StateLoading before success`() = testScope.runTest {
        // Use a channel-based flow that emits Loading then Success across coroutine yields
        val channel = kotlinx.coroutines.channels.Channel<ApiState<List<ContentEntity>>>(capacity = 10)
        whenever(repository.getDownloader(any(), any())).thenReturn(
            kotlinx.coroutines.flow.flow {
                for (item in channel) emit(item)
            }
        )
        val states = mutableListOf<UiState<List<ContentEntity>>>()
        val job = launch { viewModel.downloaderResponseState.collect { states.add(it) } }
        viewModel.getApiDownloader()
        channel.send(ApiState.Loading)
        advanceUntilIdle()
        channel.send(ApiState.Success(fakeContents))
        channel.close()
        advanceUntilIdle()
        job.cancel()
        assertTrue(states.any { it is UiState.StateLoading })
    }

    @Test
    fun `getApiDownloader updates contents on success`() = testScope.runTest {
        whenever(repository.getDownloader(any(), any())).thenReturn(
            flowOf(ApiState.Success(fakeContents))
        )
        viewModel.getApiDownloader()
        advanceUntilIdle()
        assertEquals(fakeContents, viewModel.contents)
    }

    @Test
    fun `showBadge is false when download list is empty`() = testScope.runTest {
        val values = mutableListOf<Boolean>()
        val job = launch { viewModel.showBadge.collect { values.add(it) } }
        advanceUntilIdle()
        job.cancel()
        assertTrue(values.contains(false))
    }

    @Test
    fun `isImageGeneratorFeatureActive collects true by default`() = testScope.runTest {
        val values = mutableListOf<Boolean>()
        val job = launch { viewModel.isImageGeneratorFeatureActive.collect { values.add(it) } }
        advanceUntilIdle()
        job.cancel()
        assertTrue(values.contains(true))
    }

    @Test
    fun `isGoImgFeatureActive collects true by default`() = testScope.runTest {
        val values = mutableListOf<Boolean>()
        val job = launch { viewModel.isGoImgFeatureActive.collect { values.add(it) } }
        advanceUntilIdle()
        job.cancel()
        assertTrue(values.contains(true))
    }

    @Test
    fun `incrementClickCount calls dataStore incrementClickCount`() = testScope.runTest {
        viewModel.incrementClickCount()
        advanceUntilIdle()
        verify(dataStore).incrementClickCount()
    }

    @Test
    fun `resetUrlIntent calls dataStore saveUrlIntent with empty string`() = testScope.runTest {
        viewModel.resetUrlIntent()
        advanceUntilIdle()
        verify(dataStore).saveUrlIntent("")
    }

    @Test
    fun `resumeDownload delegates to ketch`() {
        viewModel.resumeDownload(42)
        verify(ketch).resume(42)
    }

    @Test
    fun `pauseDownload delegates to ketch`() {
        viewModel.pauseDownload(42)
        verify(ketch).pause(42)
    }

    @Test
    fun `retryDownload delegates to ketch`() {
        viewModel.retryDownload(42)
        verify(ketch).retry(42)
    }

    @Test
    fun `delete delegates to ketch clearDb`() {
        viewModel.delete(42)
        verify(ketch).clearDb(42)
    }

    @Test
    fun `urlSocialMediaState default value is empty`() {
        assertEquals("", viewModel.urlSocialMediaState.text)
    }

    @Test
    fun `selectedQuality default value is empty`() {
        assertEquals("", viewModel.selectedQuality)
    }
}

