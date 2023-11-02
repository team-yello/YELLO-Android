package plugins

import ext.implementation
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

fun Project.configureAndroidCommonPlugin() {
    apply<AndroidKotlinPlugin>()
    apply<AndroidHiltPlugin>()
    with(plugins) {
        apply("kotlin-kapt")
        apply("kotlin-parcelize")
    }

    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    dependencies {
        implementation(libs.findLibrary("materialDesign").get())
    }
}