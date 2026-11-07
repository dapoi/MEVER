package plugin.module

import org.gradle.api.Plugin
import org.gradle.api.Project
import util.Dependencies.commonDependencies
import util.alias
import util.libs

class CommonModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.convention.android.library)
                alias(libs.plugins.convention.compose.library)
            }
            commonDependencies()
        }
    }
}