package com.dapascript.mever.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.dapascript.mever.build_logic.convention.ConstantLibs.COMPILER_VERSION
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configCompose(
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
            implementation(libs.androidx.activity.compose.get())
            implementation(libs.androidx.appcompat.get())
            implementation(libs.androidx.compose.material3.get())
            implementation(libs.androidx.compose.ui.tooling.preview.get())
            debugImplementation(libs.androidx.compose.ui.tooling.debug.get())
            implementation(libs.androidx.core.ktx.get())
            implementation(libs.coil.compose.get())
            implementation(libs.coil.network.get())
            implementation(libs.coil.video.get())
            implementation(libs.ketch.get())
            implementation(libs.media3.exoplayer.get())
            implementation(libs.media3.session.get())
            implementation(libs.media3.ui.get())
            implementation(libs.timber.get())
        }
    }
}