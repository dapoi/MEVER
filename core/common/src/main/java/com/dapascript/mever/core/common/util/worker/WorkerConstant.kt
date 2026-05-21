package com.dapascript.mever.core.common.util.worker

object WorkerConstant {
    /**
     * Worker Keys
     */
    const val KEY_ACTION = "action"
    const val KEY_RESULT = "result"
    const val KEY_ERROR = "error"
    const val KEY_URL = "url"
    const val KEY_QUALITY = "quality"
    const val KEY_PROMPT = "prompt"
    const val KEY_TYPE = "type"

    /**
     * Worker Actions
     */
    const val ACTION_DOWNLOAD = "download"
    const val ACTION_GENERATE_AI = "generate_ai"

    /**
     * Data Types
     */
    const val TYPE_AUDIO = "audio"
    const val TYPE_VIDEO = "video"

    /**
     * Size Limit (WorkManager Data limit is 10KB, we use 900 bytes to be safe for metadata)
     */
    const val SIZE_LIMIT = 900
    const val KEY_OUTPUT_IS_FILE = "KEY_OUTPUT_IS_FILE"
    const val KEY_OUTPUT_FILE_PATH = "KEY_OUTPUT_FILE_PATH"
}