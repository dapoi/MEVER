package com.dapascript.mever.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.dapascript.mever.build_logic.convention.ConstantLibs.COMPILER_VERSION
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = COMPILER_VERSION
        }

        dependencies {
            val bom = libs.androidx.compose.bom.get()
            implementation(platform(bom))
            androidTestImplementation(platform(bom))
            debugImplementation(libs.androidx.compose.ui.tooling.debug.get())
            implementation(libs.androidx.compose.ui.tooling.preview.get())
            implementation(libs.androidx.compose.material3.get())
        }
    }
}