package util

object ConstantLibs {
    val coreModules = listOf(
        ":core:data",
        ":core:common",
        ":core:navigation"
    )
    val resourceExcludes = listOf(
        "/META-INF/{AL2.0,LGPL2.1}",
        "/META-INF/gradle/incremental.annotation.processors",
        "**/__MACOSX/**",
        "**/__MACOSX",
        "**/.DS_Store"
    )
    val freeCompiler = listOf(
        "-opt-in=kotlin.RequiresOptIn"
    )
    val jniLibsDoNotStrip = listOf(
        "**/libandroidx.graphics.path.so",
        "**/libdatastore_shared_counter.so"
    )
    const val BASE_NAME = "com.dapascript.mever"
    const val MIN_SDK_VERSION = 26
    const val MAX_SDK_VERSION = 37
    const val KSP = "ksp"
}