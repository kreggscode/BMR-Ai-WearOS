# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep OkHttp and Retrofit classes
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class okhttp3.** { *; }
-keep class retrofit2.** { *; }

# Keep model classes
-keep class com.bmi.aipowered.model.** { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }
-keep class androidx.wear.compose.** { *; }
