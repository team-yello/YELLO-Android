// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.gradleVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.firebase:firebase-appdistribution-gradle:4.0.0")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.7")
        classpath(ClassPathPlugins.hilt)
        classpath(ClassPathPlugins.oss)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
