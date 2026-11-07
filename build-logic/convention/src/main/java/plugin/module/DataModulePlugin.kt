package plugin.module

import org.gradle.api.Plugin
import org.gradle.api.Project
import util.Dependencies.dataDependencies
import util.alias
import util.libs

class DataModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.convention.android.library)
                alias(libs.plugins.convention.hilt)
            }
            dataDependencies()
        }
    }
}