plugins {
    id("hatli.android.library")
    id("hatli.android.compose")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.app.hatli.core.ui"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
    implementation(libs.findLibrary("androidx-navigation-compose").get())
    implementation(libs.findLibrary("kotlinx-serialization-json").get())
    implementation(libs.findLibrary("kotlinx-coroutines-android").get())
}
