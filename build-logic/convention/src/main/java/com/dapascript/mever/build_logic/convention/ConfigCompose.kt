package com.dapascript.mever.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.dapascript.mever.build_logic.convention.CollectionLibs.composeDependencies
import org.gradle.api.Project

internal fun Project.configCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) = commonExtension.apply {
    buildFeatures { compose = true }
    composeDependencies()
}