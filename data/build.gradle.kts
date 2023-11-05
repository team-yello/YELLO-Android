import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
plugins {
    id("yello.android.library")
    kotlin("plugin.serialization") version libs.versions.kotlinVersion
}

android {
    buildTypes {
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                gradleLocalProperties(rootDir).getProperty("test.base.url"),
            )
        }

        release {
            buildConfigField(
                "String",
                "BASE_URL",
                gradleLocalProperties(rootDir).getProperty("base.url"),
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    namespace = "com.yello.data"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.pagingRuntime)
    implementation(libs.androidx.security)
    implementation(libs.billing)

    implementation(libs.gson)
    implementation(libs.okhttp.bom)
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.timber)
}
