package com.dapascript.mever.core.data.deeplink

import android.net.Uri
import com.dapascript.mever.core.data.deeplink.event.DeeplinkEvent
import kotlinx.coroutines.flow.Flow

interface DeeplinkManager {
    val deeplinkEvent: Flow<DeeplinkEvent?>
    suspend fun handleDeeplink(uri: Uri)
    fun clearDeeplink()
}