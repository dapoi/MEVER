package plugin.module

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import util.ProjectConstants.CORE_COMMON
import util.alias
import util.api
import util.implementation
import util.libs

class NavigationModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.convention.android.library)
                alias(libs.plugins.convention.compose.library)
                alias(libs.plugins.kotlin.serialization)
                alias(libs.plugins.kotlin.parcelize)
            }

            dependencies {
                implementation(project(CORE_COMMON))
                api(libs.androidx.navigation3.ui.get())
                api(libs.androidx.navigation3.runtime.get())
                api(libs.androidx.lifecycle.viewmodel.navigation3.get())
                api(libs.kotlinx.serialization.json.get())
            }
        }
    }
}