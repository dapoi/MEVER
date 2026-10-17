package com.dapascript.mever.feature.explore.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.data.model.local.ArtEntity
import com.dapascript.mever.core.data.repository.MeverRepository
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
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ArtImageViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repository: MeverRepository

    private val fakeArtworks = listOf(
        ArtEntity(
            id = "101",
            title = "Starry Night",
            artistDisplay = "Vincent van Gogh",
            dateDisplay = "1889",
            mediumDisplay = "Oil on canvas",
            placeOfOrigin = "France",
            imageUrl = "https://artic.edu/image1.jpg",
            thumbnailUrl = "https://artic.edu/thumb1.jpg",
            description = "A famous painting."
        )
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        whenever(repository.getArtworks(any(), any(), any())).thenReturn(
            flowOf(ApiState.Success(fakeArtworks))
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `artResponseState emits StateSuccess on successful API response`() = runTest {
        whenever(repository.getArtworks(eq("monet"), any(), any())).thenReturn(
            flowOf(ApiState.Loading, ApiState.Success(fakeArtworks))
        )
        val vm = ArtImageViewModel(repository)
        val states = mutableListOf<UiState<List<ArtEntity>>>()
        val job = launch { vm.artResponseState.collect { states.add(it) } }
        vm.getArtworks("monet")
        advanceUntilIdle()
        job.cancel()
        assertTrue(states.any { it is UiState.StateSuccess })
        assertEquals(
            fakeArtworks,
            (states.first { it is UiState.StateSuccess } as UiState.StateSuccess).data
        )
    }

    @Test
    fun `getArtworks emits StateFailed on error response`() = runTest {
        whenever(repository.getArtworks(eq("error"), any(), any())).thenReturn(
            flowOf(ApiState.Error(Throwable("Network error")))
        )
        val vm = ArtImageViewModel(repository)
        val states = mutableListOf<UiState<List<ArtEntity>>>()
        val job = launch { vm.artResponseState.collect { states.add(it) } }
        vm.getArtworks("error")
        advanceUntilIdle()
        job.cancel()
        assertTrue(states.any { it is UiState.StateFailed })
        assertEquals(
            "Network error",
            (states.first { it is UiState.StateFailed } as UiState.StateFailed).message
        )
    }

    @Test
    fun `loadNextPage appends new artworks when successful`() = runTest {
        val page2Artworks = listOf(
            ArtEntity(
                id = "102",
                title = "Sunflowers",
                artistDisplay = "Vincent van Gogh",
                dateDisplay = "1888",
                mediumDisplay = "Oil on canvas",
                placeOfOrigin = "France",
                imageUrl = "https://artic.edu/image2.jpg",
                thumbnailUrl = "https://artic.edu/thumb2.jpg",
                description = "Sunflowers painting."
            )
        )
        whenever(repository.getArtworks(eq(""), eq(1), any())).thenReturn(
            flowOf(ApiState.Success(fakeArtworks))
        )
        whenever(repository.getArtworks(eq(""), eq(2), any())).thenReturn(
            flowOf(ApiState.Success(page2Artworks))
        )

        val vm = ArtImageViewModel(repository)
        vm.getArtworks("")
        advanceUntilIdle()

        vm.loadNextPage()
        advanceUntilIdle()

        assertEquals(2, vm.currentPage)
        assertEquals(2, vm.artworks?.size)
    }

    @Test
    fun `startDownload calls repository download`() {
        val vm = ArtImageViewModel(repository)
        vm.startDownload("https://artic.edu/image1.jpg", "MEVER_ART_101.jpg")
        verify(repository).download(
            url = "https://artic.edu/image1.jpg",
            fileName = "MEVER_ART_101.jpg",
            tag = "Art"
        )
    }
}
