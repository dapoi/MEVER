package com.dapascript.mever.receiver

import android.content.BroadcastReceiver
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Toast
import androidx.core.app.RemoteInput
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.ACTION_QUICK_DOWNLOAD_DISMISS
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.ACTION_QUICK_DOWNLOAD_REPLY
import com.dapascript.mever.core.common.util.notification.QuickDownloadNotificationHelper.KEY_TEXT_REPLY
import com.dapascript.mever.core.data.source.local.MeverDataStore
import com.dapascript.mever.core.data.source.local.MeverDataStore.Companion.KEY_QUICK_NOTIF
import com.dapascript.mever.core.data.worker.MeverWorkerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class QuickDownloadReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_QUICK_DOWNLOAD_DISMISS) {
            QuickDownloadNotificationHelper.cancelQuickDownloadNotification(context)
            CoroutineScope(IO).launch {
                MeverDataStore(context.applicationContext).savePreference(KEY_QUICK_NOTIF, false)
                QuickDownloadNotificationHelper.requestTileUpdate(context)
            }
            return
        }

        if (intent.action == ACTION_QUICK_DOWNLOAD_REPLY) {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            val rawInput = remoteInput?.getCharSequence(KEY_TEXT_REPLY)?.toString()

            val inputSource = if (rawInput.isNullOrBlank()) {
                try {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                    val clipData = clipboard?.primaryClip
                    val item = if ((clipData != null) && (clipData.itemCount > 0)) clipData.getItemAt(0) else null
                    item?.text?.toString()
                } catch (_: Exception) {
                    null
                }
            } else {
                rawInput
            }

            val validUrl = extractUrl(inputSource)

            if (validUrl != null) {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context.applicationContext,
                        context.getString(R.string.quick_download_processing),
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                MeverWorkerHelper.enqueueDownloadWorker(context, validUrl)
                QuickDownloadNotificationHelper.showQuickDownloadNotification(
                    context = context,
                    isProcessing = true,
                    processingUrl = validUrl
                )
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context.applicationContext,
                        context.getString(R.string.quick_download_no_url),
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                QuickDownloadNotificationHelper.showQuickDownloadNotification(context)
            }
        }
    }

    private fun extractUrl(input: String?): String? {
        if (input.isNullOrBlank()) return null
        val text = input.trim()

        val matcher = Patterns.WEB_URL.matcher(text)
        if (matcher.find()) {
            var url = matcher.group()
            if (url != null) {
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://$url"
                }
                return url
            }
        }
        return null
    }
}