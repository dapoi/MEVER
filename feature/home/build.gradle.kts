plugins {
    alias(libs.plugins.mever.android.feature)
    alias(libs.plugins.mever.android.library.compose)
    alias(libs.plugins.mever.android.hilt)
}

android {
    namespace = "com.dapascript.mever.feature.home"
}

dependencies {
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    implementation(libs.hilt.navigation)
    implementation(libs.ketch)
}