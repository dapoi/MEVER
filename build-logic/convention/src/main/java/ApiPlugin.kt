import com.dapascript.mever.build_logic.convention.CollectionLibs.apiDependencies
import com.dapascript.mever.build_logic.convention.alias
import com.dapascript.mever.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class ApiPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.alias(libs.plugins.mever.android.library)
            apiDependencies()
        }
    }
}