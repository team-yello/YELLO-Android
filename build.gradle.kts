// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.androidKotlin) apply false
    alias(libs.plugins.androidHilt) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.android.build)
        classpath(libs.kotlin.gradle)
        classpath(libs.google.services.plugin)
        classpath(libs.google.appdistribution.gradle)
        classpath(libs.google.crashlytics.gradle)
        classpath(libs.navigation.plugin)
        classpath(libs.hilt.gradle)
        classpath(libs.google.oss.plugin)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
