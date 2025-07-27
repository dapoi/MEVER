package com.dapascript.mever.core.common.util.worker

object WorkerConstant {
    /**
     * Downloader Worker
     */
    const val KEY_REQUEST_URL = "url"
    const val KEY_REQUEST_SELECTED_QUALITY = "selected_quality"
    const val KEY_RESPONSE_CONTENTS = "contents"

    /**
     * AI Image Generator Worker
     */
    const val KEY_REQUEST_PROMPT = "prompt"
    const val KEY_RESPONSE_AI_IMAGES= "images"
    const val KEY_TOTAL_IMAGES = "total_images"

    /**
     * Error Handling
     */
    const val KEY_ERROR = "error"
}