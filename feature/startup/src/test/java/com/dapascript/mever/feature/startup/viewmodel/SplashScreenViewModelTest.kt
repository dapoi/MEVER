package com.dapascript.mever.feature.startup.viewmodel

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.state.UiState
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SplashScreenViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    lateinit var dataStore: MeverDataStore

    @Mock
    lateinit var repository: MeverRepository

    @Mock
    lateinit var context: Context

    @Mock
    lateinit var packageManager: PackageManager

    @Mock
    lateinit var packageInfo: PackageInfo

    private lateinit var viewModel: SplashScreenViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        whenever(dataStore.isOnboarded).thenReturn(flowOf(false))
        whenever(dataStore.getAppVersion).thenReturn(flowOf("1.0.0"))
        // Stub repository so init{} getAppConfig() resolves (release path)
        whenever(repository.getAppConfig()).thenReturn(flowOf())

        // Stub context/packageManager to avoid NPE in DEBUG path
        packageInfo.versionName = "1.0.0"
        whenever(context.packageName).thenReturn("com.dapascript.mever")
        whenever(context.packageManager).thenReturn(packageManager)
        whenever(packageManager.getPackageInfo("com.dapascript.mever", 0)).thenReturn(packageInfo)

        viewModel = SplashScreenViewModel(dataStore, repository, context)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isOnboarded collects false from dataStore`() = testScope.runTest {
        val values = mutableListOf<Boolean>()
        val job = launch { viewModel.isOnboarded.collect { values.add(it) } }
        advanceUntilIdle()
        assertTrue(values.contains(false))
        job.cancel()
    }

    @Test
    fun `isOnboarded collects true when dataStore returns true`() = testScope.runTest {
        whenever(dataStore.isOnboarded).thenReturn(flowOf(true))
        val vm = SplashScreenViewModel(dataStore, repository, context)
        val values = mutableListOf<Boolean>()
        val job = launch { vm.isOnboarded.collect { values.add(it) } }
        advanceUntilIdle()
        assertTrue(values.contains(true))
        job.cancel()
    }

    @Test
    fun `getAppVersion collects value from dataStore`() = testScope.runTest {
        whenever(dataStore.getAppVersion).thenReturn(flowOf("2.5.0"))
        val vm = SplashScreenViewModel(dataStore, repository, context)
        val values = mutableListOf<String>()
        val job = launch { vm.getAppVersion.collect { values.add(it) } }
        advanceUntilIdle()
        assertTrue(values.contains("2.5.0"))
        job.cancel()
    }

    @Test
    fun `appConfigState is StateInitial before coroutines resolve`() {
        assertTrue(viewModel.appConfigState.value is UiState.StateInitial)
    }

    @Test
    fun `today is a valid weekday name`() {
        val validDays = listOf(
            "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Saturday", "Sunday"
        )
        assertTrue(viewModel.today in validDays)
    }
}