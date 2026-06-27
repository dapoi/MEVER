package plugin

import com.android.build.api.dsl.LibraryExtension
import config.configAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.ConstantLibs.MAX_SDK_VERSION
import util.alias
import util.libs

class AndroidLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.library)
                alias(libs.plugins.kotlin.compose)
                alias(libs.plugins.convention.hilt)
            }

            extensions.configure<LibraryExtension> {
                configAndroid(this)
                testOptions.targetSdk = MAX_SDK_VERSION
                lint.targetSdk = MAX_SDK_VERSION
            }
        }
    }
}