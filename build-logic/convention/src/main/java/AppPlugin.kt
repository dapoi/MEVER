import com.android.build.api.dsl.ApplicationExtension
import com.dapascript.mever.build_logic.convention.ConstantLibs.MAX_SDK_VERSION
import com.dapascript.mever.build_logic.convention.ConstantLibs.resourceExcludes
import com.dapascript.mever.build_logic.convention.alias
import com.dapascript.mever.build_logic.convention.configAndroid
import com.dapascript.mever.build_logic.convention.configCompose
import com.dapascript.mever.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AppPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.application)
                alias(libs.plugins.kotlin.android)
                alias(libs.plugins.kotlin.compose)
            }

            extensions.configure<ApplicationExtension> {
                configAndroid(this)
                configCompose(this)
                defaultConfig.targetSdk = MAX_SDK_VERSION
                packaging.resources.excludes.addAll(resourceExcludes)
            }
        }
    }
}