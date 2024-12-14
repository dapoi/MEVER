import com.dapascript.mever.build_logic.convention.debugImplementation

plugins {
    alias(libs.plugins.mever.android.library)
    alias(libs.plugins.mever.android.hilt)
}

android {
    namespace = "com.dapascript.mever.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)
    implementation(libs.okhttp.interceptor)
    implementation(libs.hilt.android)
    implementation(libs.hilt.compiler)
    implementation(libs.ketch)
    implementation(libs.dataStorePreferences)
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)
}