package plugin.module

import util.alias
import util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class CommonModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.convention.android.library)
                alias(libs.plugins.convention.compose.library)
            }
        }
    }
}