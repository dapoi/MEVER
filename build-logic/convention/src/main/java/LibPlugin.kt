import com.android.build.gradle.LibraryExtension
import com.dapascript.mever.build_logic.convention.ConstantLibs.MAX_SDK_VERSION
import com.dapascript.mever.build_logic.convention.alias
import com.dapascript.mever.build_logic.convention.configAndroid
import com.dapascript.mever.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class LibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.library)
                alias(libs.plugins.kotlin.android)
                alias(libs.plugins.kotlin.compose)
            }

            extensions.configure<LibraryExtension> {
                configAndroid(this)
                defaultConfig.targetSdk = MAX_SDK_VERSION
            }
        }
    }
}