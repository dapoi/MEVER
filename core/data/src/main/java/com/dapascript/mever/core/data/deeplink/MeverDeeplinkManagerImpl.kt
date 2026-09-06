package com.dapascript.mever.core.data.deeplink

import android.net.Uri
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.DEEPLINK_HOST
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.DEEPLINK_SCHEME
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.PATH_HOME
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.QUERY_RESPONSES
import com.dapascript.mever.core.common.util.deeplink.DeeplinkConstant.QUERY_URL
import com.dapascript.mever.core.data.model.local.ContentEntity
import com.dapascript.mever.core.data.util.MoshiHelper
import com.dapascript.mever.core.data.deeplink.event.DeeplinkEvent
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

internal class MeverDeeplinkManagerImpl @Inject constructor(
    private val moshiHelper: MoshiHelper
) : MeverDeeplinkManager {

    private val _deeplinkEvent = MutableStateFlow<DeeplinkEvent?>(null)
    override val deeplinkEvent: Flow<DeeplinkEvent?> = _deeplinkEvent.asStateFlow()

    override suspend fun handleDeeplink(uri: Uri) {
        if (uri.scheme != DEEPLINK_SCHEME || uri.host != DEEPLINK_HOST) {
            _deeplinkEvent.value = DeeplinkEvent.Default
            return
        }

        val event = when (uri.path) {
            PATH_HOME -> {
                val url = uri.getQueryParameter(QUERY_URL).orEmpty()
                val responses = uri.getQueryParameter(QUERY_RESPONSES)
                if (!responses.isNullOrEmpty()) {
                    val type =
                        Types.newParameterizedType(List::class.java, ContentEntity::class.java)
                    val contents = moshiHelper.fromJson<List<ContentEntity>>(type, responses)
                    DeeplinkEvent.DownloadResult(
                        url = url,
                        contents = contents.orEmpty()
                    )
                } else {
                    DeeplinkEvent.Default
                }
            }

            else -> DeeplinkEvent.Default
        }
        _deeplinkEvent.value = event
    }

    override fun clearDeeplink() {
        _deeplinkEvent.value = null
    }
}