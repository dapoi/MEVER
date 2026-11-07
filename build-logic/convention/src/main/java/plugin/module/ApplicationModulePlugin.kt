package plugin.module

import com.android.build.api.dsl.ApplicationExtension
import config.configAndroid
import config.configCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import util.ProjectConstants.MAX_SDK_VERSION
import util.ProjectConstants.jniLibsDoNotStrip
import util.ProjectConstants.resourceExcludes
import util.alias
import util.implementation
import util.libs

class ApplicationModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.application)
                alias(libs.plugins.google.firebase.crashlytics)
                alias(libs.plugins.google.gms.google.services)
                alias(libs.plugins.kotlin.compose)
                alias(libs.plugins.convention.hilt)
            }

            extensions.configure<ApplicationExtension> {
                configAndroid(this)
                configCompose(this)
                defaultConfig.targetSdk = MAX_SDK_VERSION
                packaging {
                    resources.excludes.addAll(resourceExcludes)
                    jniLibs { jniLibs.keepDebugSymbols.addAll(jniLibsDoNotStrip) }
                }
            }

            dependencies {
                implementation(libs.firebase.crashlytics.get())
            }
        }
    }
}