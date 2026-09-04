package com.dapascript.mever.feature.startup.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var repository: MeverRepository

    private lateinit var viewModel: OnboardViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = OnboardViewModel(context, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setIsOnboarded calls repository savePreference with true`() = runTest {
        viewModel.setIsOnboarded(true)
        advanceUntilIdle()
        verify(repository).savePreference(MeverDataStore.KEY_IS_ONBOARDED, true)
    }

    @Test
    fun `setIsOnboarded calls repository savePreference with false`() = runTest {
        viewModel.setIsOnboarded(false)
        advanceUntilIdle()
        verify(repository).savePreference(MeverDataStore.KEY_IS_ONBOARDED, false)
    }
}