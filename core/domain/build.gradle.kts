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

    testImplementation(libs.findLibrary("junit").get())
    testImplementation(libs.findLibrary("mockk").get())
    testImplementation(libs.findLibrary("truth").get())
    testImplementation(libs.findLibrary("kotlinx-coroutines-test").get())
    testImplementation(libs.findLibrary("turbine").get())
}
