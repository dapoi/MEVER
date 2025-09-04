package com.dapascript.mever.core.common.util.worker

object WorkerConstant {
    /**
     * App Config Worker
     */
    const val KEY_RESPONSE_APP_CONFIG = "app_config"

    /**
     * Downloader Worker
     */
    const val KEY_REQUEST_URL = "url"
    const val KEY_REQUEST_SELECTED_QUALITY = "selected_quality"
    const val KEY_RESPONSE_CONTENTS = "contents"
    const val KEY_RESPONSE_TYPE = "type"

    /**
     * AI Image Generator Worker
     */
    const val KEY_REQUEST_PROMPT = "prompt"
    const val KEY_RESPONSE_AI_IMAGES= "images"

    /**
     * Error Handling
     */
    const val KEY_ERROR = "error"

    /**
     * Size Limit
     */
    const val SIZE_LIMIT = 900
    const val KEY_OUTPUT_IS_FILE = "KEY_OUTPUT_IS_FILE"
    const val KEY_OUTPUT_FILE_PATH = "KEY_OUTPUT_FILE_PATH"
}