package com.dapascript.mever.core.common.util

import android.content.Context
import com.dapascript.mever.core.common.R
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.NETWORK
import com.dapascript.mever.core.common.util.ErrorHandle.ErrorType.RESPONSE

object ErrorHandle {
    enum class ErrorType { NETWORK, RESPONSE }

    fun getErrorResponseContent(
        context: Context,
        message: String,
        errorType: ErrorType?
    ) = when (errorType) {
        NETWORK -> Pair(R.string.no_internet_title, context.getString(R.string.no_internet_desc))
        RESPONSE -> Pair(R.string.unknown_error_title, message)
        else -> null
    }
}