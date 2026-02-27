package com.dapascript.mever.feature.gallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.dapascript.mever.core.common.ui.theme.ThemeType
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.ketch.Ketch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
    lateinit var ketch: Ketch

    @Mock
    lateinit var dataStore: MeverDataStore

    private val savedStateHandle = SavedStateHandle(
        mapOf(
            "url" to "https://video.mp4",
            "fileName" to "video",
            "tag" to "EXPLORE",
            "isVideo" to true
        )
    )

    private lateinit var viewModel: GalleryContentDetailViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        whenever(dataStore.getTheme).thenReturn(flowOf(ThemeType.System))
        whenever(dataStore.isPipEnabled).thenReturn(flowOf(true))
        whenever(dataStore.clickCount).thenReturn(flowOf(1))

        viewModel = GalleryContentDetailViewModel(ketch, dataStore, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `themeType initial value is System`() = runTest {
        advanceUntilIdle()
        assertEquals(ThemeType.System, viewModel.themeType.value)
    }

    @Test
    fun `isPipEnabled initial value is true`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.isPipEnabled.value)
    }

    @Test
    fun `getButtonClickCount initial value is 1`() = runTest {
        advanceUntilIdle()
        assertEquals(1, viewModel.getButtonClickCount.value)
    }

    @Test
    fun `startDownload does nothing when url is blank`() {
        viewModel.startDownload("", "fileName")
        org.mockito.kotlin.verifyNoInteractions(ketch)
    }

    @Test
    fun `startDownload does nothing when url is whitespace only`() {
        viewModel.startDownload("   ", "fileName")
        org.mockito.kotlin.verifyNoInteractions(ketch)
    }

    @Test
    fun `deleteContent calls ketch clearDb`() = runTest {
        viewModel.deleteContent(99)
        advanceUntilIdle()
        verify(ketch).clearDb(99)
    }

    @Test
    fun `incrementClickCount calls dataStore incrementClickCount`() = runTest {
        viewModel.incrementClickCount()
        advanceUntilIdle()
        verify(dataStore).incrementClickCount()
    }
}