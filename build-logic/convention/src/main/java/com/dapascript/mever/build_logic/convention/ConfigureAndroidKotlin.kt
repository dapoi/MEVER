package com.dapascript.mever.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.dapascript.mever.build_logic.convention.ConstantLibs.FREE_COMPILER
import com.dapascript.mever.build_logic.convention.ConstantLibs.MAX_SDK_VERSION
import com.dapascript.mever.build_logic.convention.ConstantLibs.MIN_SDK_VERSION
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configureAndroidKotlin(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = MAX_SDK_VERSION

        defaultConfig {
            minSdk = MIN_SDK_VERSION
        }

        compileOptions {
            sourceCompatibility = VERSION_17
            targetCompatibility = VERSION_17
        }
    }

    configureKotlinCompile()
}

private fun Project.configureKotlinCompile() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JVM_17)
            freeCompilerArgs.add(FREE_COMPILER)
        }
    }
}