package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dapascript.mever.core.common.util.LanguageManager
import com.dapascript.mever.core.data.repository.MeverRepository
import com.dapascript.mever.core.data.source.local.MeverDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
class SettingLanguageViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repository: MeverRepository

    private lateinit var viewModel: SettingLanguageViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        whenever(
            repository.getPreference(
                MeverDataStore.KEY_IS_FIRST_CHANGE,
                true
            )
        ).thenReturn(flowOf(true))

        viewModel = SettingLanguageViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isFirstTimeChangeLanguage initial value is true`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.isFirstTimeChangeLanguage.value)
    }

    @Test
    fun `setIsFirstTimeChangeLanguage calls repository savePreference`() = runTest {
        viewModel.setIsFirstTimeChangeLanguage(false)
        advanceUntilIdle()
        verify(repository).savePreference(MeverDataStore.KEY_IS_FIRST_CHANGE, false)
    }

    @Test
    fun `LanguageManager appLanguages returns non-empty list`() {
        val languages = LanguageManager.appLanguages()
        assertFalse("Language list should not be empty", languages.isEmpty())
    }

    @Test
    fun `each language entry has a non-blank code`() {
        LanguageManager.appLanguages().forEach { (code, _) ->
            assertFalse("Language code '$code' should not be blank", code.isBlank())
        }
    }

    @Test
    fun `each language entry has a non-blank name`() {
        LanguageManager.appLanguages().forEach { (_, name) ->
            assertFalse("Language name '$name' should not be blank", name.isBlank())
        }
    }
}