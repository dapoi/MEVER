package com.dapascript.mever.core.data.deeplink

import android.net.Uri
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.HOST
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Path.HOME
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Path.IMAGE_GENERATOR
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Path.SPLASH
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.ART_STYLE
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.ERROR
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.PROMPT
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.RESPONSES
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.Query.URL
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.SCHEME
import com.dapascript.mever.core.data.deeplink.event.DeeplinkEvent
import com.dapascript.mever.core.data.deeplink.event.DeeplinkEvent.Default
import com.dapascript.mever.core.data.deeplink.event.DeeplinkEvent.DownloadResult
import com.dapascript.mever.core.data.deeplink.event.DeeplinkEvent.ImageGenerator
import com.dapascript.mever.core.data.deeplink.event.DeeplinkEvent.SharedUrl
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.model.local.ImageAiEntity
import com.dapascript.mever.core.data.util.MoshiHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class DeeplinkManagerImpl @Inject constructor(
    private val moshiHelper: MoshiHelper
) : DeeplinkManager {

    private val _deeplinkEvent = MutableStateFlow<DeeplinkEvent?>(null)
    override val deeplinkEvent: Flow<DeeplinkEvent?> = _deeplinkEvent.asStateFlow()

    override suspend fun handleDeeplink(uri: Uri) {
        if (uri.scheme != SCHEME || uri.host != HOST) {
            _deeplinkEvent.value = Default
            return
        }

        val event = when (uri.path) {
            SPLASH -> SharedUrl(url = uri.getQueryParameter(URL).orEmpty())
            HOME -> {
                val url = uri.getQueryParameter(URL).orEmpty()
                val responses = uri.getQueryParameter(RESPONSES).orEmpty()
                val errorMsg = uri.getQueryParameter(ERROR).orEmpty()
                val contents = moshiHelper.fromJson<List<ContentEntity>>(responses).orEmpty()

                DownloadResult(
                    url = url,
                    contents = contents,
                    errorMessage = errorMsg
                )
            }

            IMAGE_GENERATOR -> {
                val prompt = uri.getQueryParameter(PROMPT).orEmpty()
                val artStyle = uri.getQueryParameter(ART_STYLE).orEmpty()
                val response = uri.getQueryParameter(RESPONSES).orEmpty()
                val errorMessage = uri.getQueryParameter(ERROR).orEmpty()
                val imageAiEntity = moshiHelper.fromJson<ImageAiEntity>(response)

                imageAiEntity?.let {
                    ImageGenerator(
                        prompt = prompt,
                        artStyle = artStyle,
                        imageResponse = it.imagesUrl,
                        errorMessage = errorMessage
                    )
                }
            }

            else -> Default
        }
        _deeplinkEvent.value = event
    }

    override fun clearDeeplink() {
        _deeplinkEvent.value = null
    }
}