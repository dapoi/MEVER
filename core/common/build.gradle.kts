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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
}