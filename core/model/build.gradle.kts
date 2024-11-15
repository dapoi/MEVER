plugins {
    alias(libs.plugins.mever.android.library)
    alias(libs.plugins.mever.android.library.compose)
    id("kotlin-parcelize")
}

android {
    namespace = "com.dapascript.mever.core.model"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp.interceptor)
}