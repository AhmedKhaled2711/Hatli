plugins {
    id("hatli.android.feature")
}

android {
    namespace = "com.app.hatli.feature.home"
}

dependencies {
    implementation(libs.findLibrary("androidx-compose-material-icons-extended").get())
}
