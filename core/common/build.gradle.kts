plugins {
    id("hatli.android.library")
    id("hatli.android.hilt")
}

android {
    namespace = "com.app.hatli.core.common"
}

dependencies {
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
    testImplementation(libs.findLibrary("junit").get())
    testImplementation(libs.findLibrary("truth").get())
}
