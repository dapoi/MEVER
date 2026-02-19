package config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion.VERSION_21
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import util.ConstantLibs.BASE_NAME
import util.ConstantLibs.MAX_SDK_VERSION
import util.ConstantLibs.MIN_SDK_VERSION
import util.ConstantLibs.freeCompiler

internal fun Project.configAndroid(extension: Any) {
    when (extension) {
        is ApplicationExtension -> extension.configure(this)
        is LibraryExtension -> extension.configure(this)
    }
    configureKotlin()
}

private fun ApplicationExtension.configure(project: Project) = apply {
    compileSdk = MAX_SDK_VERSION
    namespace = if (project.name == "app") BASE_NAME
    else "$BASE_NAME.${project.path.replace(":", ".").substring(1)}"

    defaultConfig {
        minSdk = MIN_SDK_VERSION
        project.loadEnvProperties()
        buildConfigField(
            "String",
            "AD_BANNER_UNIT_ID",
            "\"${project.getEnvVariable("AD_BANNER_UNIT_ID")}\""
        )
        buildConfigField(
            "String",
            "AD_INTERSTITIAL_UNIT_ID",
            "\"${project.getEnvVariable("AD_INTERSTITIAL_UNIT_ID")}\""
        )
    }

    buildTypes {
        named("debug") {
            buildConfigField("String", "API_KEY", "\"${project.getEnvVariable("API_KEY_DEBUG")}\"")
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.getEnvVariable("BASE_URL_DEBUG")}\""
            )
        }
        named("release") {
            buildConfigField("String", "API_KEY", "\"\"")
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.getEnvVariable("BASE_URL_RELEASE")}\""
            )
        }
    }

    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = VERSION_21
        targetCompatibility = VERSION_21
    }
}

private fun LibraryExtension.configure(project: Project) = apply {
    compileSdk = MAX_SDK_VERSION
    namespace = "$BASE_NAME.${project.path.replace(":", ".").substring(1)}"

    defaultConfig {
        minSdk = MIN_SDK_VERSION
        project.loadEnvProperties()
        buildConfigField(
            "String",
            "AD_BANNER_UNIT_ID",
            "\"${project.getEnvVariable("AD_BANNER_UNIT_ID")}\""
        )
        buildConfigField(
            "String",
            "AD_INTERSTITIAL_UNIT_ID",
            "\"${project.getEnvVariable("AD_INTERSTITIAL_UNIT_ID")}\""
        )
    }

    buildTypes {
        named("debug") {
            buildConfigField("String", "API_KEY", "\"${project.getEnvVariable("API_KEY_DEBUG")}\"")
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.getEnvVariable("BASE_URL_DEBUG")}\""
            )
        }
        named("release") {
            buildConfigField("String", "API_KEY", "\"\"")
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${project.getEnvVariable("BASE_URL_RELEASE")}\""
            )
        }
    }

    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = VERSION_21
        targetCompatibility = VERSION_21
    }
}

private fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JVM_21)
            freeCompilerArgs.addAll(freeCompiler)
        }
    }
}

private fun Project.loadEnvProperties() {
    val apiConfigFile = rootProject.file("./env.properties")
    if (apiConfigFile.exists()) {
        apiConfigFile.forEachLine { line ->
            val entry = line.split("=", limit = 2)
            if (entry.size == 2) rootProject.extra.set(entry[0].trim(), entry[1].trim())
        }
    }
}

fun Project.getEnvVariable(
    key: String
) = rootProject.extra.get(key)?.toString()?.takeIf { it.isNotBlank() }.orEmpty()