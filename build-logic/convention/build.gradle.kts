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
        // Registering the plugins for different module types
        register("application") {
            id = "convention.application"
            implementationClass = "plugin.module.ApplicationModulePlugin"
        }
        register("common") {
            id = "convention.common"
            implementationClass = "plugin.module.CommonModulePlugin"
        }
        register("data") {
            id = "convention.data"
            implementationClass = "plugin.module.DataModulePlugin"
        }
        register("feature") {
            id = "convention.feature"
            implementationClass = "plugin.module.FeatureModulePlugin"
        }
        register("navigation") {
            id = "convention.navigation"
            implementationClass = "plugin.module.NavigationModulePlugin"
        }

        // Registering the plugins for Android, Compose, and Hilt
        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "plugin.AndroidLibPlugin"
        }
        register("composeLibrary") {
            id = "convention.compose.library"
            implementationClass = "plugin.ComposeLibPlugin"
        }
        register("hiltLibrary") {
            id = "convention.hilt"
            implementationClass = "plugin.HiltLibPlugin"
        }
    }
}