import com.dapascript.mever.build_logic.convention.debugImplementation

plugins {
    alias(libs.plugins.mever.library)
    alias(libs.plugins.mever.hilt)
}

android {
    namespace = "com.dapascript.mever.core.data"

    defaultConfig {
        val apiConfigFile = rootProject.file("./build-properties/env.properties")
        (apiConfigFile.exists()).let {
            apiConfigFile.forEachLine { line ->
                val entry = line.split("=", limit = 2)
                if (entry.size == 2) {
                    rootProject.extra.set(entry[0].trim(), entry[1].trim())
                }
            }
        }

        val baseUrl = rootProject.extra.get("BASE_URL")?.toString()?.takeIf { it.isNotBlank() } ?: "\"\""
        buildConfigField("String", "BASE_URL", baseUrl)
    }

    buildFeatures { buildConfig = true }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp.interceptor)
    implementation(libs.dataStorePreferences)
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)
}