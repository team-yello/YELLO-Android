plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.android.build)
    compileOnly(libs.kotlin.gradle)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "yello.android.application"
            implementationClass = "plugins.AndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "yello.android.library"
            implementationClass = "plugins.AndroidLibraryPlugin"
        }
        register("androidHilt") {
            id = "yello.android.androidHilt"
            implementationClass = "plugins.AndroidHiltPlugin"
        }
        register("androidKotlin") {
            id = "yello.android.kotlin"
            implementationClass = "plugins.AndroidKotlinPlugin"
        }
    }
}
