package com.dapascript.mever.build_logic.convention

object ConstantLibs {
    val coreModules = listOf(
        ":core:data",
        ":core:common",
        ":core:navigation"
    )
    val resourceExcludes = listOf(
        "/META-INF/{AL2.0,LGPL2.1}",
        "/META-INF/gradle/incremental.annotation.processors"
    )
    const val BASE_NAME = "com.dapascript.mever"
    const val MIN_SDK_VERSION = 26
    const val MAX_SDK_VERSION = 36
    const val KSP = "ksp"
    const val FREE_COMPILER = "-opt-in=kotlin.RequiresOptIn"
}