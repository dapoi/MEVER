import com.dapascript.mever.build_logic.convention.implementation

plugins {
    alias(libs.plugins.mever.android.application)
    alias(libs.plugins.mever.android.application.compose)
    alias(libs.plugins.mever.android.hilt)
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
    implementation(projects.feature.home)
    implementation(projects.feature.notification)
    implementation(projects.feature.setting)
    implementation(projects.core.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.compose)
}
