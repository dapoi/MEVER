package com.dapascript.mever.feature.gallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.PlatformType
import com.ketch.DownloadModel
import com.ketch.Ketch
import com.ketch.Status
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GalleryLandingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var ketch: Ketch

    private lateinit var viewModel: GalleryLandingViewModel

    private fun fakeDownloadModel(
        id: Int = 1,
        tag: String = PlatformType.ALL.platformName,
        status: Status = Status.SUCCESS,
        fileName: String = "video.mp4"
    ) = DownloadModel(
        url = "https://video.mp4",
        path = "/storage",
        fileName = fileName,
        tag = tag,
        id = id,
        headers = hashMapOf(),
        timeQueued = 0L,
        status = status,
        total = 100L,
        progress = 100,
        speedInBytePerMs = 0f,
        lastModified = 0L,
        eTag = "",
        metaData = "",
        failureReason = ""
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(ketch.observeDownloads()).thenReturn(flowOf(emptyList()))
        viewModel = GalleryLandingViewModel(ketch)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectedFilter default is ALL`() {
        assertEquals(PlatformType.ALL, viewModel.selectedFilter)
    }

    @Test
    fun `selectedItems is empty by default`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.selectedItems.value.isEmpty())
    }

    @Test
    fun `toggleSelection adds item to selection`() = runTest {
        val item = fakeDownloadModel()
        viewModel.toggleSelection(item)
        advanceUntilIdle()
        assertTrue(viewModel.selectedItems.value.contains(item))
    }

    @Test
    fun `toggleSelection removes item if already selected`() = runTest {
        val item = fakeDownloadModel()
        viewModel.toggleSelection(item)
        viewModel.toggleSelection(item)
        advanceUntilIdle()
        assertFalse(viewModel.selectedItems.value.contains(item))
    }

    @Test
    fun `toggleSelectionAll selects all items`() = runTest {
        val items = listOf(fakeDownloadModel(1), fakeDownloadModel(2))
        viewModel.toggleSelectionAll(items)
        advanceUntilIdle()
        assertEquals(items.toSet(), viewModel.selectedItems.value)
    }

    @Test
    fun `clearSelection empties selection`() = runTest {
        val item = fakeDownloadModel()
        viewModel.toggleSelection(item)
        viewModel.clearSelection()
        advanceUntilIdle()
        assertTrue(viewModel.selectedItems.value.isEmpty())
    }

    @Test
    fun `resumeDownload delegates to ketch`() {
        viewModel.resumeDownload(1)
        verify(ketch).resume(1)
    }

    @Test
    fun `pauseDownload delegates to ketch`() {
        viewModel.pauseDownload(1)
        verify(ketch).pause(1)
    }

    @Test
    fun `pauseAllDownloads delegates to ketch`() {
        viewModel.pauseAllDownloads()
        verify(ketch).pauseAll()
    }

    @Test
    fun `retryDownload delegates to ketch`() {
        viewModel.retryDownload(1)
        verify(ketch).retry(1)
    }

    @Test
    fun `delete delegates to ketch clearDb`() {
        viewModel.delete(1)
        verify(ketch).clearDb(1)
    }

    @Test
    fun `deleteAll delegates to ketch clearAllDb`() {
        viewModel.deleteAll()
        verify(ketch).clearAllDb()
    }
}