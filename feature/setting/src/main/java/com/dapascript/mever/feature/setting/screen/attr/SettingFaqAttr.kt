package com.dapascript.mever.feature.setting.screen.attr

import com.dapascript.mever.core.common.R

object SettingFaqAttr {
    data class FaqUiModel(
        val id: Int,
        val question: Int,
        val answer: Int,
        val isExpanded: Boolean = false,
        val isLink: Boolean = false
    )

    val faqList = listOf(
        FaqUiModel(
            id = 1,
            question = R.string.faq_q1,
            answer = R.string.faq_a1
        ),
        FaqUiModel(
            id = 2,
            question = R.string.faq_q2,
            answer = R.string.faq_a2
        ),
        FaqUiModel(
            id = 3,
            question = R.string.faq_q3,
            answer = R.string.faq_a3,
            isLink = true
        ),
        FaqUiModel(
            id = 4,
            question = R.string.faq_q4,
            answer = R.string.faq_a4
        ),
        FaqUiModel(
            id = 5,
            question = R.string.faq_q5,
            answer = R.string.faq_a5
        ),
        FaqUiModel(
            id = 6,
            question = R.string.faq_q6,
            answer = R.string.faq_a6
        ),
        FaqUiModel(
            id = 7,
            question = R.string.faq_q7,
            answer = R.string.faq_a7
        ),
        FaqUiModel(
            id = 8,
            question = R.string.faq_q8,
            answer = R.string.faq_a8
        ),
        FaqUiModel(
            id = 9,
            question = R.string.faq_q9,
            answer = R.string.faq_a9
        )
    )
}
