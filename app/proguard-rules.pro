# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Suppress R8 warnings about kotlinx-serialization placeholder syntax
# The warning comes from kotlinx-serialization-common.pro using "<1>$*" patterns
# This is a known issue with kotlinx-serialization 1.9.0 and R8
-ignorewarnings

# Alternative: Use -dontwarn for the specific library causing the issue
-dontwarn kotlinx.serialization.**
-dontwarn org.jetbrains.kotlinx.**

# Keep kotlinx-serialization annotations
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep Serializable classes
-keepclassmembers @kotlinx.serialization.Serializable class * {
    *** Companion;
}

# Keep names of serializable classes
-keepnames @kotlinx.serialization.Serializable class *

# Suppress the specific warning about placeholder syntax in kotlinx-serialization rules
# This suppresses warnings about types like "<1>$*" which are ProGuard placeholders
-dontwarn kotlinx.serialization.internal.**
-dontwarn kotlinx.serialization.json.internal.**

# Additional rule to suppress warnings about synthetic companion objects
-dontwarn **$serializer
-dontwarn **$$serializer
