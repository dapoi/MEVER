import com.dapascript.mever.build_logic.convention.ConstantLibs.coreModules
import com.dapascript.mever.build_logic.convention.alias
import com.dapascript.mever.build_logic.convention.implementation
import com.dapascript.mever.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.mever.library)
                alias(libs.plugins.mever.navigation)
                alias(libs.plugins.mever.library.compose)
            }

            dependencies {
                coreModules.forEach { module -> implementation(project(module)) }
            }
        }
    }
}