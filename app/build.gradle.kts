import config.getEnvVariable

plugins {
    alias(libs.plugins.convention.application)
}

android {
    defaultConfig {
        applicationId = "com.dapascript.mever"
        versionCode = 20250831
        versionName = "1.2.0"

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
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }

    bundle {
        language {
            enableSplit = false
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