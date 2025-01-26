plugins {
    alias(libs.plugins.mever.library)
    alias(libs.plugins.mever.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.dapascript.mever.core.model"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp.interceptor)
}