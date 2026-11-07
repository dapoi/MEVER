package plugin.library

import com.android.build.api.dsl.LibraryExtension
import config.configCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.alias
import util.libs

class ComposeLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.alias(libs.plugins.kotlin.compose)
            extensions.configure<LibraryExtension> { configCompose(this) }
        }
    }
}