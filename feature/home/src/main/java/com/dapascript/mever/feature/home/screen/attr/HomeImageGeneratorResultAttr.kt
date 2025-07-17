package com.dapascript.mever.feature.home.screen.attr

import android.content.Context
import com.dapascript.mever.core.common.R

object HomeImageGeneratorResultAttr {
    data class MenuAction(
        val title: String,
        val icon: Int
    )

    fun getMenuActions(
        context: Context,
        hasCopied: Boolean
    ) = listOf(
        MenuAction(
            title = context.getString(if (hasCopied) R.string.copied else R.string.copy_prompt),
            icon = R.drawable.ic_copy
        ),
        MenuAction(
            title = context.getString(R.string.download_all),
            icon = R.drawable.ic_download
        ),
        MenuAction(
            title = context.getString(R.string.report),
            icon = R.drawable.ic_report
        ),
        MenuAction(
            title = context.getString(R.string.share),
            icon = R.drawable.ic_share
        ),
    )
}