# Hatli Application ProGuard / R8 Rules

# Preserve line numbers and source file names for crash reporting
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Preserve Kotlinx Serialization runtime classes and generated serializers
-keepattributes *Annotation*, InnerClasses, Signature
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
-keepclassmembers class * {
    *** Companion;
}
-keepclasseswithmembers class * {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class * implements kotlinx.serialization.KSerializer {
    <init>(...);
}

# Preserve Room Database Entities and DAOs
-keep class * extends androidx.room.RoomDatabase
-keep class com.app.hatli.core.database.entity.** { *; }
-keep interface com.app.hatli.core.database.dao.** { *; }
