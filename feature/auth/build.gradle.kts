plugins {
    id("hatli.android.feature")
}

android {
    namespace = "com.app.hatli.feature.auth"
}

dependencies {
    implementation(libs.findLibrary("androidx-compose-foundation").get())
    implementation(libs.findLibrary("androidx-compose-material-icons-extended").get())
}
