import com.dapascript.mever.build_logic.convention.alias
import com.dapascript.mever.build_logic.convention.implementation
import com.dapascript.mever.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class NavPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.kotlin.serialization)
                alias(libs.plugins.kotlin.parcelize)
            }

            dependencies {
                implementation(libs.androidx.navigation.compose.get())
                implementation(libs.kotlinx.serialization.json.get())
            }
        }
    }
}