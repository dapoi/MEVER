import com.dapascript.mever.build_logic.convention.implementation

plugins {
    alias(libs.plugins.mever.application)
    alias(libs.plugins.mever.application.compose)
    alias(libs.plugins.mever.hilt)
}

android {
    namespace = "com.dapascript.mever"

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
    implementation(projects.feature.startup)
    implementation(projects.feature.home)
    implementation(projects.feature.notification)
    implementation(projects.feature.setting)
    implementation(projects.feature.gallery)
    implementation(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.ketch)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}