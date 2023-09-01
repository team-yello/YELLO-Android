import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.google.firebase.crashlytics")
    id("androidx.navigation.safeargs")
}

android {
    namespace = Constants.packageName
    compileSdk = Constants.compileSdk

    defaultConfig {
        applicationId = Constants.packageName
        minSdk = Constants.minSdk
        targetSdk = Constants.targetSdk
        versionCode = Constants.versionCode
        versionName = Constants.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

            buildConfigField(
                "String",
                "AMPLITUDE_API_KEY",
                gradleLocalProperties(rootDir).getProperty("amplitude.api.test.key"),
            )

            manifestPlaceholders["NATIVE_APP_KEY"] =
                gradleLocalProperties(rootDir).getProperty("testNativeAppKey")
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

            buildConfigField(
                "String",
                "AMPLITUDE_API_KEY",
                gradleLocalProperties(rootDir).getProperty("amplitude.api.key"),
            )

            manifestPlaceholders["NATIVE_APP_KEY"] =
                gradleLocalProperties(rootDir).getProperty("nativeAppKey")

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = Versions.javaVersion
        targetCompatibility = Versions.javaVersion
    }

    kotlinOptions {
        jvmTarget = Versions.jvmVersion
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core-ui"))
    implementation(project(":data"))
    implementation(project(":domain"))

    KotlinDependencies.run {
        implementation(kotlin)
        implementation(coroutines)
        implementation(jsonSerialization)
        implementation(dateTime)
        implementation(gson)
    }

    AndroidXDependencies.run {
        implementation(coreKtx)
        implementation(appCompat)
        implementation(constraintLayout)
        implementation(fragment)
        implementation(startup)
        implementation(legacy)
        implementation(security)
        implementation(hilt)
        implementation(lifeCycleKtx)
        implementation(lifecycleJava8)
        implementation(splashScreen)
        implementation(pagingRuntime)
        implementation(workManager)
        implementation(hiltWorkManager)
        implementation(billing)
        implementation(navigationFragment)
        implementation(navigationUi)
        implementation(navigationDynamic)
    }

    FirebaseDependencies.run {
        implementation(platform(bom))
        implementation(messaging)
        implementation(analytics)
        implementation(crashlytics)
        implementation(remoteConfig)
    }

    KaptDependencies.run {
        kapt(hiltCompiler)
        kapt(hiltWorkManagerCompiler)
    }

    implementation(MaterialDesignDependencies.materialDesign)
    TestDependencies.run {
        testImplementation(jUnit)
        androidTestImplementation(androidTest)
        androidTestImplementation(espresso)
    }

    ThirdPartyDependencies.run {
        implementation(coil)
        implementation(platform(okHttpBom))
        implementation(okHttp)
        implementation(okHttpLoggingInterceptor)
        implementation(retrofit)
        implementation(retrofitJsonConverter)
        implementation(timber)
        implementation(ossLicense)
        implementation(progressView)
        implementation(balloon)
        implementation(lottie)
        implementation(circularProgressBar)
        implementation(kakaoLogin)
        implementation(kakaoAuth)
        implementation(kakaoTalk)
        implementation(kakaoShare)
        implementation(circleIndicator)
        implementation(flipper)
        implementation(flipperNetwork)
        implementation(flipperLeakCanary)
        implementation(soloader)
        implementation(shimmer)
        implementation(amplitude)
    }

    GoogleDependencies.run {
        implementation(inAppUpdate)
    }
}
