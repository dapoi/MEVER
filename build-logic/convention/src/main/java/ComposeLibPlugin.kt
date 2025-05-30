import com.android.build.api.dsl.LibraryExtension
import com.dapascript.mever.build_logic.convention.alias
import com.dapascript.mever.build_logic.convention.configCompose
import com.dapascript.mever.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ComposeLibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.alias(libs.plugins.android.library)
            extensions.configure<LibraryExtension> { configCompose(this) }
        }
    }
}