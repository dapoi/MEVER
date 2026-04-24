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

# --- Optional: Create feature subfolders ---
if [ "$PARENT_FOLDER" = "feature" ]; then
  echo "📦 Setting up feature packages (navigation, screen, viewmodel)..."
  mkdir -p $FULL_PATH/src/main/java/$PACKAGE_PATH/navigation
  mkdir -p $FULL_PATH/src/main/java/$PACKAGE_PATH/screen
  mkdir -p $FULL_PATH/src/main/java/$PACKAGE_PATH/viewmodel
fi
# ----------------------------------

# Create build.gradle.kts
if [ "$PARENT_FOLDER" = "feature" ]; then

  # if it's a feature module, we apply the convention plugin instead of the standard library plugin
  cat <<EOL > $FULL_PATH/build.gradle.kts
plugins {
    alias(libs.plugins.convention.feature)
}
EOL

else

  # if it's not a feature module, we apply the standard library plugin with default dependencies
  cat <<EOL > $FULL_PATH/build.gradle.kts
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "$BASE_PACKAGE.$PARENT_FOLDER.$MODULE_NAME"
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

fi

# Update settings.gradle.kts
if grep -Fxq "$GRADLE_INCLUDE" settings.gradle.kts; then
    echo "✅ Module already included in settings.gradle.kts. Skipping."
else
    tail -c1 settings.gradle.kts | read -r _ || echo >> settings.gradle.kts
    echo "$GRADLE_INCLUDE" >> settings.gradle.kts
    echo "✅ Added ':$PARENT_FOLDER:$MODULE_NAME' to settings.gradle.kts"
fi

# ========================================
# UPDATE APP MODULE DEPENDENCIES
# ========================================
APP_GRADLE="app/build.gradle.kts"
DEPENDENCY_LINE="implementation(projects.$PARENT_FOLDER.$MODULE_NAME)"

if grep -q "$DEPENDENCY_LINE" "$APP_GRADLE"; then
    echo "✅ Module already added to $APP_GRADLE. Skipping."
else
    # Use awk to insert the dependency line right after the 'dependencies {' line
    awk -v dep="    $DEPENDENCY_LINE" '
    /^dependencies \{/ {
        print
        print dep
        next
    }
    1
    ' "$APP_GRADLE" > "${APP_GRADLE}.tmp" && mv "${APP_GRADLE}.tmp" "$APP_GRADLE"

    echo "✅ Added '$DEPENDENCY_LINE' to $APP_GRADLE"
fi

echo "✅ Module ':$PARENT_FOLDER:$MODULE_NAME' generated successfully."