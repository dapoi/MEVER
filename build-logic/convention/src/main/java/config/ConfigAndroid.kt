package config

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion.VERSION_21
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.ConstantLibs.BASE_NAME
import util.ConstantLibs.FREE_COMPILER
import util.ConstantLibs.MAX_SDK_VERSION
import util.ConstantLibs.MIN_SDK_VERSION

internal fun Project.configAndroid(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        compileSdk = MAX_SDK_VERSION
        namespace = (if (project.name == "app") BASE_NAME
        else "$BASE_NAME.${project.path.replace(":", ".").substring(1)}")

        defaultConfig {
            minSdk = MIN_SDK_VERSION
            val apiConfigFile = rootProject.file("./env.properties")
            (apiConfigFile.exists()).let {
                apiConfigFile.forEachLine { line ->
                    val entry = line.split("=", limit = 2)
                    if (entry.size == 2) rootProject.extra.set(entry[0].trim(), entry[1].trim())
                }
            }
            buildConfigField(
                "String",
                "AD_BANNER_UNIT_ID",
                "\"${getEnvVariable("AD_BANNER_UNIT_ID")}\""
            )
            buildConfigField(
                "String",
                "AD_INTERSTITIAL_UNIT_ID",
                "\"${getEnvVariable("AD_INTERSTITIAL_UNIT_ID")}\""
            )
        }

        buildTypes {
            getByName("debug") {
                buildConfigField(
                    "String",
                    "API_KEY",
                    "\"${getEnvVariable("API_KEY_DEBUG")}\""
                )
                buildConfigField(
                    "String",
                    "BASE_URL",
                    "\"${getEnvVariable("BASE_URL_DEBUG")}\""
                )
            }

            getByName("release") {
                buildConfigField(
                    "String",
                    "API_KEY",
                    "\"\""
                )
                buildConfigField(
                    "String",
                    "BASE_URL",
                    "\"${getEnvVariable("BASE_URL_RELEASE")}\""
                )
            }
        }

        buildFeatures {
            buildConfig = true
        }

        compileOptions {
            sourceCompatibility = VERSION_21
            targetCompatibility = VERSION_21
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JVM_21)
            freeCompilerArgs.add(FREE_COMPILER)
        }
    }
}

fun Project.getEnvVariable(key: String) =
    rootProject.extra.get(key)?.toString()?.takeIf { it.isNotBlank() } ?: ""