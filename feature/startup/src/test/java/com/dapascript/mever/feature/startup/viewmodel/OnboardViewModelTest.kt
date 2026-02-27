package com.dapascript.mever.feature.startup.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class OnboardViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var dataStore: MeverDataStore

    private lateinit var viewModel: OnboardViewModel

    @Test
    fun `setIsOnboarded calls dataStore setIsOnboarded with true`() = runBlocking {
        MockitoAnnotations.openMocks(this@OnboardViewModelTest)
        viewModel = OnboardViewModel(dataStore)
        viewModel.setIsOnboarded(true)
        delay(100)
        verify(dataStore).setIsOnboarded(true)
    }

    @Test
    fun `setIsOnboarded calls dataStore setIsOnboarded with false`() = runBlocking {
        MockitoAnnotations.openMocks(this@OnboardViewModelTest)
        viewModel = OnboardViewModel(dataStore)
        viewModel.setIsOnboarded(false)
        delay(100)
        verify(dataStore).setIsOnboarded(false)
    }
}