package com.dapascript.mever.feature.setting.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingFaqViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `faqList is not empty`() {
        val viewModel = SettingFaqViewModel()
        assertTrue(viewModel.faqList.value.isNotEmpty())
    }

    @Test
    fun `faq items are collapsed by default`() {
        val viewModel = SettingFaqViewModel()
        assertFalse(viewModel.faqList.value.any { it.isExpanded })
    }

    @Test
    fun `onExpand expands the target item`() {
        val viewModel = SettingFaqViewModel()
        val targetId = viewModel.faqList.value.first().id

        viewModel.onExpand(targetId)

        val target = viewModel.faqList.value.first { it.id == targetId }
        assertTrue(target.isExpanded)
    }

    @Test
    fun `onExpand collapses the target item when expanded twice`() {
        val viewModel = SettingFaqViewModel()
        val targetId = viewModel.faqList.value.first().id

        viewModel.onExpand(targetId)
        viewModel.onExpand(targetId)

        val target = viewModel.faqList.value.first { it.id == targetId }
        assertFalse(target.isExpanded)
    }

    @Test
    fun `onExpand keeps other items unchanged`() {
        val viewModel = SettingFaqViewModel()
        val targetId = viewModel.faqList.value.first().id
        val otherIds = viewModel.faqList.value.map { it.id }.filter { it != targetId }

        viewModel.onExpand(targetId)

        otherIds.forEach { otherId ->
            val other = viewModel.faqList.value.first { it.id == otherId }
            assertFalse(other.isExpanded)
        }
    }
}