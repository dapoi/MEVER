package com.dapascript.mever.feature.home.screen.section.attr

import android.content.Context
import com.dapascript.mever.feature.home.R
import com.dapascript.mever.core.common.R as coreUiR

object HomeAiSectionAttr {
    data class StyleOption(
        val styleName: String,
        val promptKeywords: String,
        val image: Int
    )

    fun getArtStyles(context: Context) = with(context) {
        listOf(
            StyleOption(
                styleName = getString(coreUiR.string.dreamy),
                promptKeywords = "dreamy, soft colors, ethereal, pastel tones",
                image = R.drawable.dreamy
            ),
            StyleOption(
                styleName = getString(coreUiR.string.cinematic),
                promptKeywords = "cinematic lighting, film still, dramatic composition",
                image = R.drawable.cinematic
            ),
            StyleOption(
                styleName = getString(coreUiR.string.futuristic),
                promptKeywords = "futuristic, neon lights, sci-fi style, high-tech",
                image = R.drawable.futuristic
            ),
            StyleOption(
                styleName = getString(coreUiR.string.painterly),
                promptKeywords = "oil painting, brush strokes, traditional art",
                image = R.drawable.oil
            )
        )
    }
}