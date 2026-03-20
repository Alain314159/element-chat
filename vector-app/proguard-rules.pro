# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /sdk/tools/proguard/proguard-android.txt

# Custom rules
-keep class com.mardous.cerlita.** { *; }
-keep class im.vector.app.features.romantic.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Lottie
-keep class com.airbnb.lottie.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Matrix SDK
-keep class org.matrix.android.** { *; }
-keep class org.matrix.olm.** { *; }

# Epoxy
-keep class com.airbnb.android.epoxy.** { *; }

# Mavericks
-keep class com.airbnb.android.mavericks.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Kotlin
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Moshi
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn okio.**
-keep class com.squareup.moshi.** { *; }
-keep class kotlin.Metadata { *; }

# Retrofit
-dontwarn retrofit2.KotlinExtensions
-keep class retrofit2.** { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Realm
-keep class io.realm.** { *; }
-keep class io.realm.internal.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# LruCache
-keep class * implements androidx.collection.LruCache

# Default rules
-dontoptimize
-dontobfuscate
