plugins {
    id("hatli.android.library")
    id("hatli.android.compose")
}

android {
    namespace = "com.app.hatli.core.designsystem"
}

dependencies {
    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-compose-foundation").get())
    implementation(libs.findLibrary("androidx-compose-material-icons-extended").get())
}
