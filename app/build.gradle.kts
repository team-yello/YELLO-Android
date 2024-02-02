plugins {
    id("yello.android.application")
    id("yello.android.androidHilt")
    alias(libs.plugins.androidKotlin)
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":data"))
    implementation(project(":domain"))
}
