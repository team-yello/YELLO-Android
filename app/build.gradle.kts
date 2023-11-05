plugins {
    id("yello.android.application")
    id("yello.android.androidHilt")
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":data"))
    implementation(project(":domain"))
}
