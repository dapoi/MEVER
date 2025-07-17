package com.dapascript.mever.feature.home.screen.attr

import android.content.Context
import com.dapascript.mever.feature.home.R
import com.dapascript.mever.core.common.R as coreUiR

object HomeLandingScreenAttr {
    data class StyleOption(
        val styleName: String,
        val promptKeywords: String,
        val image: Int
    )

    fun getInspirePrompt() = listOf(
        "A lonely robot discovering an ancient forest",
        "A futuristic city floating in the clouds",
        "A dreamy landscape with giant glowing mushrooms",
        "A cinematic moment of two strangers meeting at a station",
        "An astronaut landing in an alien underwater world",
        "An oil painting of life in the 31st century",
        "Flying cats in a pastel-colored sky",
        "A time traveler arrives in a neon-lit future city",
        "A portrait of a woman from a mystical forest kingdom",
        "A child playing among purple-colored clouds",
        "A cyberpunk hero standing in the neon rain",
        "A sunset scene like an indie romance movie",
        "A secret garden hidden in the sky",
        "A post-apocalyptic world in soft pastel colors",
        "A fantasy character walking on a bridge of light",
        "A battle between galactic creatures in a futuristic city",
        "A little girl looking at Earth from the moon",
        "An ancient illustration of a city that never existed",
        "A light festival in a floating village",
        "A surreal painting of an endless dream"
    ).random()

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