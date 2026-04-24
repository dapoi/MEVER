#!/usr/bin/env bash

echo "
 █████╗ ███╗ ███╗  ██████╗ 
██╔══██╗████╗████║██╔════╝ 
███████║██╔████╔██║██║  ██╗
██╔══██║██║╚██╔╝██║██║  ╚██╗
██║  ██║██║ ╚═╝ ██║╚██████╔╝
╚═╝  ╚═╝╚═╝     ╚═╝ ╚═════╝ 

🚀 Android Module Generator
👤 Created by : Luthfi Daffa Prabowo
🔗 GitHub     : https://github.com/dapoi
💼 LinkedIn   : https://linkedin.com/in/luthfi-daffa-prabowo
"

# ========================================
# START PROMPT
# ========================================

read -p "📦 Base package (example: com.project.compose): " BASE_PACKAGE
if [ -z "$BASE_PACKAGE" ]; then
  echo "❌ Base package cannot be empty."
  exit 1
fi

read -p "📁 Parent folder (example: feature): " PARENT_FOLDER
if [ -z "$PARENT_FOLDER" ]; then
  echo "❌ Parent folder cannot be empty."
  exit 1
fi

read -p "🧩 Module name (example: home): " MODULE_NAME
if [ -z "$MODULE_NAME" ]; then
  echo "❌ Module name cannot be empty."
  exit 1
fi

# ========================================
# BUILD STRUCTURE
# ========================================

FULL_PATH="$PARENT_FOLDER/$MODULE_NAME"
PACKAGE_PATH=$(echo $BASE_PACKAGE | tr '.' '/')/$PARENT_FOLDER/$MODULE_NAME
GRADLE_INCLUDE="include(\":$PARENT_FOLDER:$MODULE_NAME\")"

# Check existing module
if [ -d "$FULL_PATH" ]; then
  echo "⚠️  Module '$FULL_PATH' already exists. Exiting."
  exit 1
fi

echo "🚀 Creating module '$FULL_PATH' with package '$BASE_PACKAGE.$PARENT_FOLDER.$MODULE_NAME'..."

# Create folders
mkdir -p $FULL_PATH/src/main/java/$PACKAGE_PATH
mkdir -p $FULL_PATH/src/main/res

# Create build.gradle.kts
cat <<EOL > $FULL_PATH/build.gradle.kts
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.home"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
EOL

# Update settings.gradle.kts
if grep -Fxq "$GRADLE_INCLUDE" settings.gradle.kts; then
    echo "✅ Module already included in settings.gradle.kts. Skipping."
else
    tail -c1 settings.gradle.kts | read -r _ || echo >> settings.gradle.kts
    echo "$GRADLE_INCLUDE" >> settings.gradle.kts
    echo "✅ Added ':$PARENT_FOLDER:$MODULE_NAME' to settings.gradle.kts"
fi

echo "✅ Module ':$PARENT_FOLDER:$MODULE_NAME' generated successfully."
