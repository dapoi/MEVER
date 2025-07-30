package util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.ConstantLibs.KSP
import util.ConstantLibs.coreModules

object CollectionLibs {
    fun Project.commonDependencies() {
        dependencies {
            val bom = libs.androidx.compose.bom.get()
            implementation(platform(bom))
            androidTestImplementation(platform(bom))
            implementation(libs.ads.get())
            implementation(libs.androidx.activity.compose.get())
            implementation(libs.androidx.appcompat.get())
            implementation(libs.androidx.compose.material3.get())
            implementation(libs.androidx.compose.ui.tooling.preview.get())
            debugImplementation(libs.androidx.compose.ui.tooling.debug.get())
            implementation(libs.androidx.core.ktx.get())
            implementation(libs.coil.compose.get())
            implementation(libs.coil.network.get())
            implementation(libs.coil.video.get())
            implementation(libs.firebase.crashlytics.get())
            implementation(libs.google.play.update.core.get())
            implementation(libs.google.play.update.ktx.get())
            implementation(libs.ketch.get())
            implementation(libs.media3.exoplayer.get())
            implementation(libs.media3.session.get())
            implementation(libs.media3.ui.get())
            implementation(libs.timber.get())
        }
    }

    fun Project.dataDependencies() {
        dependencies {
            implementation(project(coreModules[1]))
            implementation(libs.dataStorePreferences.get())
            implementation(libs.moshi.kotlin.get())
            implementation(libs.okhttp.interceptor.get())
            implementation(libs.retrofit.lib.get())
            implementation(libs.retrofit.moshi.get())
            implementation(libs.timber.get())
            implementation(libs.work.runtime.get())
            add(KSP, libs.moshi.codegen.get())
            debugImplementation(libs.chucker.debug.get())
            releaseImplementation(libs.chucker.release.get())
        }
    }
}