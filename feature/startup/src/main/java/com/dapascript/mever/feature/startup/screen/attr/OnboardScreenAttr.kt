package com.dapascript.mever.feature.startup.screen.attr

internal object OnboardScreenAttr {
    internal data class OnboardPage(
        val image: Int,
        val title: String,
        val highlightedText: String,
        val description: String,
        val subtitle: String
    )
}