import config.getEnvVariable
import util.implementation

plugins {
    alias(libs.plugins.convention.application)
}

android {
    androidResources {
        @Suppress("UnstableApiUsage")
        generateLocaleConfig = true
    }

    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("KEYSTORE_PATH") ?: ""
            if (keystorePath.isNotEmpty()) {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEY_STORE_PASSWORD") ?: ""
                keyAlias = System.getenv("KEY_ALIAS") ?: ""
                keyPassword = System.getenv("KEY_PASSWORD") ?: ""
            }
        }
    }

    defaultConfig {
        applicationId = "com.dapascript.mever"
        versionCode = project.findProperty("versionCode")?.toString()?.toInt() ?: 1
        versionName = project.findProperty("versionName")?.toString() ?: "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val admobIdValue = getEnvVariable("ADMOB_ID").ifEmpty {
            "ca-app-pub-3940256099942544~3347511713"
        }
        manifestPlaceholders["admobId"] = admobIdValue
        buildConfigField("String", "ADMOB_ID", "\"$admobIdValue\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = if (System.getenv("KEYSTORE_PATH")?.isNotEmpty() == true) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }
        }

        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    configurations.configureEach {
        exclude(group = "com.google.android.gms", module = "play-services-ads")
        exclude(group = "com.google.android.gms", module = "play-services-ads-lite")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.feature.ai)
    implementation(projects.feature.explore)
    implementation(projects.feature.gallery)
    implementation(projects.feature.home)
    implementation(projects.feature.setting)
    implementation(projects.feature.startup)
    implementation(projects.feature.wa)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.arch.core.testing)
}