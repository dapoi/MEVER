plugins {
    alias(libs.plugins.mever.feature)
    alias(libs.plugins.mever.library.compose)
    alias(libs.plugins.mever.hilt)
}

android {
    namespace = "com.dapascript.mever.feature.home"
}

dependencies {
    implementation(libs.ketch)
    implementation(libs.coil)
    implementation(libs.coil.video)
    implementation(libs.coil.network)
}