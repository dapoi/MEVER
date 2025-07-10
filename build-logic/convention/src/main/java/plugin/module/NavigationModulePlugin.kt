package plugin.module

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.ConstantLibs.coreModules
import util.alias
import util.implementation
import util.libs

class NavigationModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.convention.android.library)
                alias(libs.plugins.kotlin.serialization)
                alias(libs.plugins.kotlin.parcelize)
            }

            dependencies {
                implementation(project(coreModules[1]))
                implementation(libs.androidx.navigation.compose.get())
                implementation(libs.kotlinx.serialization.json.get())
            }
        }
    }
}