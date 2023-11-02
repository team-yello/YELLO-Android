package plugins

import com.android.build.gradle.LibraryExtension
import ext.androidTestImplementation
import ext.application.kotlinOptions
import ext.coreLibraryDesugaring
import ext.getBundle
import ext.getLibrary
import ext.getVersionCatalog
import ext.implementation
import ext.kapt
import ext.testImplementation
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Android Library 모듈에 적용할 Plugin
 *
 * plugin id : [yello.android.library]
 */
class AndroidLibraryPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureAndroidCommonPlugin()

                compileSdk = Constants.compileSdk
                defaultConfig {
                    minSdk = Constants.minSdk

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                kotlinOptions {
                    jvmTarget = Constants.jvmVersion
                }
            }

            val libs = extensions.getVersionCatalog()
            dependencies {
                // test
                testImplementation(libs.getLibrary("jUnit"))
                androidTestImplementation(libs.getLibrary("androidTest"))
                androidTestImplementation(libs.getLibrary("espresso"))

                // hilt
                implementation(libs.getLibrary("hilt"))
                kapt(libs.getLibrary("hiltAndroidCompiler"))
            }
        }
    }
}
