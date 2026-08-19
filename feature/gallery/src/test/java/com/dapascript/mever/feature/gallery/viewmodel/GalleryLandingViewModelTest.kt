package com.dapascript.mever.feature.gallery.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.PlatformType
import com.dapascript.mever.core.data.repository.MeverRepository
import com.ketch.DownloadModel
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
    lateinit var repository: MeverRepository

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
        whenever(repository.observeDownloads()).thenReturn(flowOf(emptyList()))
        viewModel = GalleryLandingViewModel(repository)
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
    fun `resumeDownload delegates to repository`() {
        viewModel.resumeDownload(1)
        verify(repository).resumeDownload(1)
    }

    @Test
    fun `pauseDownload delegates to repository`() {
        viewModel.pauseDownload(1)
        verify(repository).pauseDownload(1)
    }

    @Test
    fun `pauseAllDownloads delegates to repository`() {
        viewModel.pauseAllDownloads()
        verify(repository).pauseAllDownloads()
    }

    @Test
    fun `retryDownload delegates to repository`() {
        viewModel.retryDownload(1)
        verify(repository).retryDownload(1)
    }

    @Test
    fun `delete calls repository deleteDownload`() {
        viewModel.delete(1)
        verify(repository).deleteDownload(1)
    }

    @Test
    fun `deleteAll calls repository deleteAllDownloads`() {
        viewModel.deleteAll()
        verify(repository).deleteAllDownloads()
    }
}