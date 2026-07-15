package com.dapascript.mever.feature.ai.screen.attr

import android.content.Context
import com.dapascript.mever.core.common.R

object AiImageGeneratorAttr {
    data class StyleOption(
        val styleName: String,
        val promptKeywords: String,
        val image: Int
    )

    fun getArtStyles(context: Context) = with(context) {
        listOf(
            StyleOption(
                styleName = getString(R.string.dreamy),
                promptKeywords = "dreamy, soft colors, ethereal, pastel tones",
                image = R.drawable.dreamy
            ),
            StyleOption(
                styleName = getString(R.string.cinematic),
                promptKeywords = "cinematic lighting, film still, dramatic composition",
                image = R.drawable.cinematic
            ),
            StyleOption(
                styleName = getString(R.string.futuristic),
                promptKeywords = "futuristic, neon lights, sci-fi style, high-tech",
                image = R.drawable.futuristic
            ),
            StyleOption(
                styleName = getString(R.string.painterly),
                promptKeywords = "oil painting, brush strokes, traditional art",
                image = R.drawable.oil
            )
        )
    }
}