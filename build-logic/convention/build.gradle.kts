import org.gradle.api.JavaVersion.VERSION_21
import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.dapascript.mever.build_logic.convention"

java {
    sourceCompatibility = VERSION_21
    targetCompatibility = VERSION_21
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JVM_21)
        freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    gradle.serviceOf<DependenciesAccessors>().classes.asFiles.forEach {
        compileOnly(files(it.absolutePath))
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("application") {
            id = "mever.application"
            implementationClass = "AppPlugin"
        }
        register("api") {
            id = "mever.api"
            implementationClass = "ApiPlugin"
        }
        register("feature") {
            id = "mever.feature"
            implementationClass = "FeaturePlugin"
        }
        register("hilt") {
            id = "mever.hilt"
            implementationClass = "HiltPlugin"
        }
        register("androidLibrary") {
            id = "mever.android.library"
            implementationClass = "AndroidLibPlugin"
        }
        register("ComposeLibrary") {
            id = "mever.compose.library"
            implementationClass = "ComposeLibPlugin"
        }
        register("navigation") {
            id = "mever.navigation"
            implementationClass = "NavPlugin"
        }
    }
}