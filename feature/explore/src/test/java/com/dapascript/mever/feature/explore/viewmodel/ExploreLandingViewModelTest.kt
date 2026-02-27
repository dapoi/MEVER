package com.dapascript.mever.feature.explore.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.state.ApiState
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.repository.MeverRepository
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
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ExploreLandingViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repository: MeverRepository

    private val fakeContents = listOf(
        ContentEntity(url = "https://img1.jpg", status = true, id = "1"),
        ContentEntity(url = "https://img2.jpg", status = true, id = "2")
    )

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        // stub for init{} call in the ViewModel (DEBUG=false path uses empty string query)
        whenever(repository.getImageSearch(any())).thenReturn(
            flowOf(ApiState.Success(fakeContents))
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `exploreResponseState is StateInitial before coroutines run`() {
        val vm = ExploreLandingViewModel(repository)
        assertTrue(vm.exploreResponseState.value is UiState.StateInitial)
    }

    @Test
    fun `getExploreContents emits StateSuccess on successful API response`() = runTest {
        whenever(repository.getImageSearch("nature")).thenReturn(
            flowOf(ApiState.Loading, ApiState.Success(fakeContents))
        )
        val vm = ExploreLandingViewModel(repository)
        vm.getExploreContents("nature")
        advanceUntilIdle()
        val state = vm.exploreResponseState.value
        assertTrue(state is UiState.StateSuccess)
        assertEquals(fakeContents, (state as UiState.StateSuccess).data)
    }

    @Test
    fun `getExploreContents emits StateFailed on error response`() = runTest {
        whenever(repository.getImageSearch("fail")).thenReturn(
            flowOf(ApiState.Error(Throwable("Network error")))
        )
        val vm = ExploreLandingViewModel(repository)
        vm.getExploreContents("fail")
        advanceUntilIdle()
        val state = vm.exploreResponseState.value
        assertTrue(state is UiState.StateFailed)
        assertEquals("Network error", (state as UiState.StateFailed).message)
    }

    @Test
    fun `getExploreContents emits StateLoading on loading response`() = runTest {
        whenever(repository.getImageSearch("tech")).thenReturn(
            flowOf(ApiState.Loading)
        )
        val vm = ExploreLandingViewModel(repository)
        vm.getExploreContents("tech")
        advanceUntilIdle()
        assertTrue(vm.exploreResponseState.value is UiState.StateLoading)
    }

    @Test
    fun `query default value is empty string`() {
        val vm = ExploreLandingViewModel(repository)
        assertEquals("", vm.query)
    }

    @Test
    fun `query can be updated`() {
        val vm = ExploreLandingViewModel(repository)
        vm.query = "wallpaper"
        assertEquals("wallpaper", vm.query)
    }
}