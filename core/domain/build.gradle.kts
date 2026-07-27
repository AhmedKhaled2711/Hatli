plugins {
    id("hatli.android.library")
}

android {
    namespace = "com.app.hatli.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
    implementation(libs.findLibrary("javax-inject").get())
}
