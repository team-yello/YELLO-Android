plugins {
    id("yello.android.library")
    id("yello.android.library.compose")
}

android {
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    namespace = "com.yello.ui"
}

dependencies {
    // Lifecycle Ktx
    implementation(libs.androidx.lifeCycleKtx)
}
