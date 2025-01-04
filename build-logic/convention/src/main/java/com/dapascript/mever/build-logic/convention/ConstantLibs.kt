package com.dapascript.mever.build_logic.convention

object ConstantLibs {
    val coreModules = listOf(
        ":core:model",
        ":core:data",
        ":core:common"
    )
    val resourceExcludes = listOf(
        "/META-INF/{AL2.0,LGPL2.1}",
        "/META-INF/gradle/incremental.annotation.processors"
    )

    const val COMPILER_VERSION = "1.5.21"
    const val MIN_SDK_VERSION = 26
    const val MAX_SDK_VERSION = 35
    const val KSP = "ksp"
    const val FREE_COMPILER = "-opt-in=kotlin.RequiresOptIn"
}