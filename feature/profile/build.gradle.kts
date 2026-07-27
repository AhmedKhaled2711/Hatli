plugins {
    id("hatli.android.feature")
}

android {
    namespace = "com.app.hatli.feature.profile"
}

dependencies {
    implementation(libs.findLibrary("coil-compose").get())
}
