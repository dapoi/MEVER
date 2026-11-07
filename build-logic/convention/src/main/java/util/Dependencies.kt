package util

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.ProjectConstants.CORE_COMMON
import util.ProjectConstants.KSP

object Dependencies {
    fun Project.composeDependencies() {
        dependencies {
            val bom = libs.androidx.compose.bom.get()
            implementation(platform(bom))
            androidTestImplementation(platform(bom))
            implementation(libs.androidx.activity.compose.get())
            implementation(libs.androidx.appcompat.get())
            implementation(libs.androidx.compose.material3.get())
            implementation(libs.androidx.compose.materialWindow.get())
            implementation(libs.androidx.compose.ui.tooling.preview.get())
            debugImplementation(libs.androidx.compose.ui.tooling.debug.get())
            implementation(libs.androidx.core.ktx.get())
            implementation(libs.coil.compose.get())
            implementation(libs.coil.network.get())
            implementation(libs.coil.video.get())
        }
    }

    fun Project.commonDependencies() {
        dependencies {
            api(libs.ads.get())
            api(libs.google.play.update.core.get())
            api(libs.google.play.update.ktx.get())
            api(libs.ketch.get())
            api(libs.media3.exoplayer.get())
            api(libs.media3.session.get())
            api(libs.media3.ui.get())
            api(libs.timber.get())
            implementation(libs.mlkit.subject.segmentation.get())
            implementation(libs.play.services.base.get())
        }
    }

    fun Project.dataDependencies() {
        dependencies {
            implementation(project(CORE_COMMON))
            implementation(libs.dataStorePreferences.get())
            api(libs.ketch.get())
            implementation(libs.moshi.kotlin.get())
            implementation(libs.okhttp.interceptor.get())
            implementation(libs.retrofit.lib.get())
            implementation(libs.retrofit.moshi.get())
            api(libs.revenuecat.purchases.get())
            implementation(libs.timber.get())
            implementation(libs.work.runtime.get())
            add(KSP, libs.moshi.codegen.get())
            debugImplementation(libs.chucker.debug.get())
            releaseImplementation(libs.chucker.release.get())
        }
    }

    fun Project.testDependencies() {
        dependencies {
            testImplementation(libs.coroutines.test.get())
            testImplementation(libs.mockito.core.get())
            testImplementation(libs.mockito.kotlin.get())
            testImplementation(libs.arch.core.testing.get())
        }
    }
}