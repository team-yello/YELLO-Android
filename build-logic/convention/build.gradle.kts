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
        register("androidHilt") {
            id = "yello.android.hilt"
            implementationClass = "plugins.AndroidHiltPlugin"
        }
        register("androidKotlin") {
            id = "yello.android.kotlin"
            implementationClass = "plugins.AndroidKotlinPlugin"
        }
    }
}
