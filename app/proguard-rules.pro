# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# --- Custom ProGuard Rules ---

# Keep line numbers for Crashlytics/Play Console stack traces
-keepattributes SourceFile,LineNumberTable

# Renames source files to "SourceFile" for consistent mapping.
# Upload the R8 mapping file from app/build/outputs/mapping/release/mapping.txt
# to Play Console for readable stack traces.
-renamesourcefileattribute SourceFile

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.health.calculator.bmi.tracker.data.model.** { *; }

# Compose
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Room
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Hilt
-keep,allowobfuscation,allowshrinking @dagger.hilt.android.AndroidEntryPoint class *
-keep,allowobfuscation,allowshrinking @dagger.hilt.android.HiltAndroidApp class *

# Firebase Analytics is loaded through a small reflection bridge so the
# provider remains optional at compile time. Keep the public entry points for
# opted-in release builds; collection is still disabled by default.
-keep class com.google.firebase.analytics.FirebaseAnalytics { *; }

# Health Connect client — the SDK uses reflection internally for record
# type resolution. Keep the record classes used by the app.
-keep class androidx.health.connect.client.records.StepsRecord { *; }
-keep class androidx.health.connect.client.records.WeightRecord { *; }
-keep class androidx.health.connect.client.** { *; }
-dontwarn androidx.health.connect.client.**

# Coil
-keep class coil.** { *; }

# Google Drive API
-keep class com.google.api.services.drive.** { *; }
-keep class com.google.api.client.** { *; }
-dontwarn org.apache.http.**
-dontwarn org.ietf.jgss.**
-dontwarn sun.misc.Unsafe
-dontwarn javax.naming.**
-dontwarn javax.net.ssl.**
