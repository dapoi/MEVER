plugins {
    alias(libs.plugins.mever.feature)
    alias(libs.plugins.mever.library.compose)
}

android {
    namespace = "com.dapascript.mever.feature.gallery"
}

dependencies {
    implementation(libs.ketch)
    implementation(libs.coil)
    implementation(libs.coil.video)
    implementation(libs.coil.network)
}