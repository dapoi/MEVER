package plugin.module

import org.gradle.api.Plugin
import org.gradle.api.Project
import util.CollectionLibs.dataDependencies
import util.alias
import util.libs

class DataModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.alias(libs.plugins.convention.android.library)
            dataDependencies()
        }
    }
}