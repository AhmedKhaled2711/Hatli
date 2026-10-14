plugins {
    id("hatli.android.library")
    id("hatli.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.app.hatli.core.network"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField("String", "BASE_URL", "\"https://api.hatli.app/\"")
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.findLibrary("retrofit").get())
    implementation(libs.findLibrary("retrofit-kotlinx-serialization").get())
    implementation(libs.findLibrary("okhttp").get())
    implementation(libs.findLibrary("okhttp-logging").get())
    implementation(libs.findLibrary("kotlinx-serialization-json").get())
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
}
