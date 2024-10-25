plugins {
    alias(libs.plugins.mever.android.feature)
    alias(libs.plugins.mever.android.library.compose)
    alias(libs.plugins.mever.android.hilt)
}

android {
    namespace = "com.dapascript.mever.feature.setting"
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    implementation(libs.hilt.navigation)
}