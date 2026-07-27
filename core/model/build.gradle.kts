plugins {
    id("hatli.android.library")
}

android {
    namespace = "com.app.hatli.core.model"
}

dependencies {
    implementation(project(":core:common"))
}
