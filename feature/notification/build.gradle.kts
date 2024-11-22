plugins {
    alias(libs.plugins.mever.android.feature)
    alias(libs.plugins.mever.android.library.compose)
    alias(libs.plugins.mever.android.hilt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.dapascript.mever.feature.notification"
}

dependencies {
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    implementation(libs.hilt.navigation)
    implementation(libs.ketch)
    implementation(libs.coil)
    implementation(libs.coil.video)
}