plugins {
    id("hatli.android.library")
    id("hatli.android.hilt")
}

android {
    namespace = "com.app.hatli.core.database"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.findLibrary("room-runtime").get())
    implementation(libs.findLibrary("room-ktx").get())
    "ksp"(libs.findLibrary("room-compiler").get())
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
}
