package plugin.library

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.ProjectConstants.KSP
import util.alias
import util.implementation
import util.libs

class HiltPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.hilt)
                alias(libs.plugins.ksp)
            }

            dependencies {
                implementation(libs.hilt.android.get())
                implementation(libs.hilt.navigation.get())
                implementation(libs.hilt.work.get())
                add(KSP, libs.hilt.compiler.google.get())
                add(KSP, libs.hilt.compiler.ext.get())
            }
        }
    }
}