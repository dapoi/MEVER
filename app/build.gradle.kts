import config.getEnvVariable

plugins {
    alias(libs.plugins.convention.application)
}

android {
    defaultConfig {
        applicationId = "com.dapascript.mever"
        versionCode = 20250727
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        manifestPlaceholders["admobId"] = getEnvVariable("ADMOB_ID").ifEmpty {
            "ca-app-pub-3940256099942544~3347511713"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.feature.gallery)
    implementation(projects.feature.home)
    implementation(projects.feature.setting)
    implementation(projects.feature.startup)
}