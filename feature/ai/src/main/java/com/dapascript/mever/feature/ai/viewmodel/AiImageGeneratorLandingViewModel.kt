package com.dapascript.mever.feature.ai.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.dapascript.mever.core.common.base.BaseViewModel
import com.dapascript.mever.feature.ai.screen.attr.AiImageGeneratorLandingAttr.StyleOption
import com.dapascript.mever.feature.ai.screen.attr.AiImageGeneratorLandingAttr.getArtStyles
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
internal class AiImageGeneratorLandingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : BaseViewModel() {
    val artStyles by lazy { getArtStyles(context) }
    var prompt by mutableStateOf("")
    var artStyleSelected by mutableStateOf<StyleOption?>(null)
}