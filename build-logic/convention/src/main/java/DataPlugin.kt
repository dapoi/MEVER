import com.dapascript.mever.build_logic.convention.ConstantLibs.coreModules
import com.dapascript.mever.build_logic.convention.debugImplementation
import com.dapascript.mever.build_logic.convention.implementation
import com.dapascript.mever.build_logic.convention.libs
import com.dapascript.mever.build_logic.convention.releaseImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class DataPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(project(coreModules[1]))
                implementation(libs.dataStorePreferences.get())
                implementation(libs.okhttp.interceptor.get())
                implementation(libs.retrofit.lib.get())
                implementation(libs.retrofit.converter.get())
                implementation(libs.timber.get())
                debugImplementation(libs.chucker.debug.get())
                releaseImplementation(libs.chucker.release.get())
            }
        }
    }
}