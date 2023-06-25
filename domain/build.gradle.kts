plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    KotlinDependencies.run {
        implementation(kotlin)
        implementation(coroutines)
        implementation(dateTime)
    }
    ThirdPartyDependencies.run {
        implementation(hiltCore)
    }
    KaptDependencies.run {
        kapt(hiltCompiler)
    }
}