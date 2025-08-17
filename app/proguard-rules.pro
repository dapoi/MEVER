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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep attributes needed for reflection and generics
-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault
-keepattributes InnerClasses,EnclosingMethod

# Keep generic signatures for proper type casting
-keepattributes Signature

# Keep all enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelable classes
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Keep Serializable classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep data classes and their constructors
-keep class com.dapascript.mever.** { *; }
-keepclassmembers class com.dapascript.mever.** {
    <fields>;
    <methods>;
}

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }

# Keep Kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}

# Keep Retrofit and Moshi related classes
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# Keep Moshi adapters
-keep class com.squareup.moshi.** { *; }
#noinspection ShrinkerUnresolvedReference
-keep @com.squareup.moshi.JsonQualifier interface *
-keepclassmembers class * {
    #noinspection ShrinkerUnresolvedReference
    @com.squareup.moshi.Json <fields>;
}

# Keep Hilt/Dagger generated classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.AndroidEntryPoint { *; }
-keep class **_HiltModules { *; }
-keep class **_HiltModules$* { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

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
