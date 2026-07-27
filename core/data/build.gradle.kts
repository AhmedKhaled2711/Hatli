plugins {
    id("hatli.android.library")
    id("hatli.android.hilt")
}

android {
    namespace = "com.app.hatli.core.data"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
    implementation(libs.findLibrary("androidx-datastore-preferences").get())

    implementation(platform(libs.findLibrary("firebase-bom").get()))
    implementation(libs.findLibrary("firebase-auth").get())
    implementation(libs.findLibrary("firebase-firestore").get())
    implementation(libs.findLibrary("firebase-storage").get())
}
