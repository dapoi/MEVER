import org.gradle.initialization.DependenciesAccessors
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.dapascript.mever.build_logic.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
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
        register("androidApplication") {
            id = "mever.application"
            implementationClass = "AppPlugin"
        }
        register("androidApplicationCompose") {
            id = "mever.application.compose"
            implementationClass = "AppComposePlugin"
        }
        register("androidData") {
            id = "mever.data"
            implementationClass = "DataPlugin"
        }
        register("androidFeature") {
            id = "mever.feature"
            implementationClass = "FeaturePlugin"
        }
        register("androidHilt") {
            id = "mever.hilt"
            implementationClass = "HiltPlugin"
        }
        register("androidLibrary") {
            id = "mever.library"
            implementationClass = "LibPlugin"
        }
        register("androidLibraryCompose") {
            id = "mever.library.compose"
            implementationClass = "LibComposePlugin"
        }
        register("androidNav") {
            id = "mever.navigation"
            implementationClass = "NavPlugin"
        }
    }
}