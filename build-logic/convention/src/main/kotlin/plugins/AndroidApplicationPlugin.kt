package plugins

import Constants
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import ext.androidTestImplementation
import ext.application.configureDefault
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
 * Android Application 모듈에 적용할 Plugin
 *
 * plugin id : [yello.android.application]
 */
class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) =
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("androidx.navigation.safeargs")
                apply("com.google.android.gms.oss-licenses-plugin")
                apply("com.google.gms.google-services")
                apply("com.google.firebase.appdistribution")
                apply("com.google.firebase.crashlytics")
            }


            extensions.configure<ApplicationExtension> {
                namespace = Constants.packageName
                compileSdk = Constants.compileSdk

                configureAndroidCommonPlugin()
                configureDefault()

                buildFeatures {
                    buildConfig = true
                    viewBinding = true
                    dataBinding = true
                }

                buildTypes {
                    debug {
                        buildConfigField(
                            "String",
                            "BASE_URL",
                            gradleLocalProperties(rootDir).getProperty("test.base.url"),
                        )

                        buildConfigField(
                            "String",
                            "NATIVE_APP_KEY",
                            gradleLocalProperties(rootDir).getProperty("test.native.app.key"),
                        )

                        manifestPlaceholders["NATIVE_APP_KEY"] =
                            gradleLocalProperties(rootDir).getProperty("testNativeAppKey")

                        buildConfigField(
                            "String",
                            "AMPLITUDE_API_KEY",
                            gradleLocalProperties(rootDir).getProperty("amplitude.api.test.key"),
                        )

                        buildConfigField(
                            "String",
                            "ADMOB_REWARD_KEY",
                            gradleLocalProperties(rootDir).getProperty("test.admob.reward.key"),
                        )

                        manifestPlaceholders["ADMOB_REWARD_KEY"] =
                            gradleLocalProperties(rootDir).getProperty("testAdmobRewardKey")
                    }

                    release {
                        buildConfigField(
                            "String",
                            "BASE_URL",
                            gradleLocalProperties(rootDir).getProperty("base.url"),
                        )

                        buildConfigField(
                            "String",
                            "NATIVE_APP_KEY",
                            gradleLocalProperties(rootDir).getProperty("native.app.key"),
                        )

                        manifestPlaceholders["NATIVE_APP_KEY"] =
                            gradleLocalProperties(rootDir).getProperty("nativeAppKey")

                        buildConfigField(
                            "String",
                            "AMPLITUDE_API_KEY",
                            gradleLocalProperties(rootDir).getProperty("amplitude.api.key"),
                        )

                        buildConfigField(
                            "String",
                            "ADMOB_REWARD_KEY",
                            gradleLocalProperties(rootDir).getProperty("admob.reward.key"),
                        )

                        manifestPlaceholders["ADMOB_REWARD_KEY"] =
                            gradleLocalProperties(rootDir).getProperty("admobRewardKey")

                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }
            }

            val libs = extensions.getVersionCatalog()
            dependencies {
                // androidx
                implementation(libs.getBundle("androidx"))

                // firebase
                implementation(platform(libs.getLibrary("firebase-bom")))
                implementation(libs.getBundle("firebase"))

                // flipper
                implementation(libs.getBundle("flipper"))

                // retrofit
                implementation(libs.getBundle("retrofit"))

                // test
                testImplementation(libs.getLibrary("jUnit"))
                androidTestImplementation(libs.getLibrary("androidTest"))
                androidTestImplementation(libs.getLibrary("espresso"))

                // google
                implementation(libs.getLibrary("inAppUpdate"))
                implementation(libs.getLibrary("ossLicense"))
                implementation(libs.getLibrary("gson"))

                // okhttp
                implementation(platform(libs.getLibrary("okhttp-Bom")))
                implementation(libs.getBundle("okhttp"))

                // kakao
                implementation(libs.getBundle("kakao"))

                // hilt
                implementation(libs.getLibrary("hilt"))
                kapt(libs.getLibrary("hiltAndroidCompiler"))
                kapt(libs.getLibrary("hiltWorkManagerCompiler"))

                implementation(libs.getBundle("appModuleLibraryEtc"))
            }
        }
}
