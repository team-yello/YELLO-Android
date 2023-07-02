plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Constants.compileSdk

    defaultConfig {
        minSdk = Constants.minSdk
        targetSdk = Constants.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }
    kotlinOptions {
        jvmTarget = Versions.jvmVersion
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    namespace = "com.yello.ui"
}

dependencies {
    // Kotlin
    implementation(KotlinDependencies.kotlin)

    // Lifecycle Ktx
    implementation(AndroidXDependencies.lifeCycleKtx)

    // Material Design
    implementation(MaterialDesignDependencies.materialDesign)

    // Hilt
    implementation(AndroidXDependencies.hilt)
    kapt(KaptDependencies.hiltAndroidCompiler)

    // Test Dependency
    testImplementation(TestDependencies.jUnit)
    androidTestImplementation(TestDependencies.androidTest)
    androidTestImplementation(TestDependencies.espresso)
}
