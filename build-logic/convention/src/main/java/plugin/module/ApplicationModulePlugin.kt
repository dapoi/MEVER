package plugin.module

import com.android.build.api.dsl.ApplicationExtension
import config.configAndroid
import config.configCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.ConstantLibs.MAX_SDK_VERSION
import util.ConstantLibs.resourceExcludes
import util.alias
import util.libs

class ApplicationModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.application)
                alias(libs.plugins.google.firebase.crashlytics)
                alias(libs.plugins.google.gms.google.services)
                alias(libs.plugins.kotlin.android)
                alias(libs.plugins.kotlin.compose)
                alias(libs.plugins.convention.hilt)
            }

            extensions.configure<ApplicationExtension> {
                configAndroid(this)
                configCompose(this)
                defaultConfig.targetSdk = MAX_SDK_VERSION
                packaging.resources.excludes.addAll(resourceExcludes)
            }
        }
    }
}