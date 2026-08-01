package com.dapascript.mever.feature.startup.screen.attr

object OnboardScreenAttr {
    data class OnboardPage(
        val image: Int,
        val title: String,
        val highlightedText: String,
        val description: String,
        val subtitle: String
    )
}