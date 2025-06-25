package com.dapascript.mever.core.common.util

import android.content.ClipData.newPlainText
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE

fun copyToClipboard(context: Context, content: String) {
    val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = newPlainText(content, content)
    clipboardManager.setPrimaryClip(clipData)
}

fun pasteFromClipboard(context: Context): String? {
    val clipboardManager = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    return if (clipboardManager.hasPrimaryClip()) {
        clipboardManager.primaryClip?.getItemAt(0)?.text?.toString()
    } else null
}