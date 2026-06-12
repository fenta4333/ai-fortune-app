# ==========================================
# AI Fortune App ProGuard / R8 Rules
# ==========================================

# ---- Keep model classes for JSON serialization ----
-keep class com.aifortune.app.domain.model.** { *; }
-keep class com.aifortune.app.data.remote.** { *; }

# ---- Retrofit ----
-keepattributes Signature, Exceptions, InnerClasses, EnclosingMethod
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# ---- Gson ----
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# ---- Kotlin Serialization ----
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.aifortune.app.**$$serializer { *; }
-keepclassmembers class com.aifortune.app.** {
    *** Companion;
}
-keepclasseswithmembers class com.aifortune.app.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ---- Compose ----
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# ---- Hilt / Dagger ----
-dontwarn dagger.**
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ---- Coroutines ----
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# ---- OkHttp ----
-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**

# ---- General ----
-keepattributes LineNumberTable
-keepattributes SourceFile
-renamesourcefileattribute SourceFile