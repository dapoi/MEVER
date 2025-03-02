import com.dapascript.mever.build_logic.convention.implementation

plugins {
    alias(libs.plugins.mever.application)
    alias(libs.plugins.mever.application.compose)
    alias(libs.plugins.mever.hilt)
}

android {
    defaultConfig {
        applicationId = "com.dapascript.mever"
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.feature.gallery)
    implementation(projects.feature.home)
    implementation(projects.feature.setting)
    implementation(projects.feature.startup)
}