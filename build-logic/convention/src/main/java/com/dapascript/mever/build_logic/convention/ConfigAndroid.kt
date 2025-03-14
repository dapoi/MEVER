package com.dapascript.mever.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.dapascript.mever.build_logic.convention.ConstantLibs.BASE_NAME
import com.dapascript.mever.build_logic.convention.ConstantLibs.FREE_COMPILER
import com.dapascript.mever.build_logic.convention.ConstantLibs.MAX_SDK_VERSION
import com.dapascript.mever.build_logic.convention.ConstantLibs.MIN_SDK_VERSION
import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

internal fun Project.configAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = MAX_SDK_VERSION
        namespace = (if (project.name == "app") BASE_NAME
        else "$BASE_NAME.${project.path.replace(":", ".").substring(1)}")

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            val apiConfigFile = rootProject.file("./build-properties/env.properties")
            (apiConfigFile.exists()).let {
                apiConfigFile.forEachLine { line ->
                    val entry = line.split("=", limit = 2)
                    if (entry.size == 2) rootProject.extra.set(entry[0].trim(), entry[1].trim())
                }
            }
            val baseUrl = rootProject.extra.get("BASE_URL")?.toString()?.takeIf { it.isNotBlank() } ?: "\"\""
            buildConfigField("String", "BASE_URL", baseUrl)
        }

        buildFeatures {
            buildConfig = true
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