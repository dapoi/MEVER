plugins {
    alias(libs.plugins.mever.android.library)
    alias(libs.plugins.mever.android.library.compose)
    alias(libs.plugins.mever.android.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.dapascript.mever.core.common"
}

dependencies {
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ketch)
    implementation(libs.coil)
    implementation(libs.coil.video)
    implementation(libs.coil.network)
    implementation(libs.lottie)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)
}