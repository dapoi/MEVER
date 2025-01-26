import com.android.build.gradle.LibraryExtension
import com.dapascript.mever.build_logic.convention.ConstantLibs.MAX_SDK_VERSION
import com.dapascript.mever.build_logic.convention.alias
import com.dapascript.mever.build_logic.convention.configureAndroidKotlin
import com.dapascript.mever.build_logic.convention.implementation
import com.dapascript.mever.build_logic.convention.libs
import com.dapascript.mever.build_logic.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class LibPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                alias(libs.plugins.android.library)
                alias(libs.plugins.kotlin.android)
                alias(libs.plugins.kotlin.compose)
            }

            extensions.configure<LibraryExtension> {
                configureAndroidKotlin(this)
                defaultConfig.targetSdk = MAX_SDK_VERSION
            }

            dependencies {
                implementation(libs.timber)
                testImplementation(kotlin("test"))
            }
        }
    }
}