plugins {
    alias(libs.plugins.mever.library)
    alias(libs.plugins.mever.library.compose)
}

android {
    namespace = "com.dapascript.mever.core.model"
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp.interceptor)
}