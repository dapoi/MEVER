import com.dapascript.mever.build_logic.convention.debugImplementation

plugins {
    alias(libs.plugins.mever.library)
    alias(libs.plugins.mever.hilt)
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
    implementation(libs.dataStorePreferences)
    debugImplementation(libs.chucker.debug)
    releaseImplementation(libs.chucker.release)
}