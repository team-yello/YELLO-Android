package ext

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *>
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.14"
        }
        val libs = extensions.getVersionCatalog()

        dependencies {
            val composeBom = platform(libs.getLibrary("compose-bom"))
            implementation(composeBom)
            androidTestImplementation(composeBom)
            implementation(libs.getBundle("compose-bom-bundle"))
            debugImplementation(libs.getLibrary("compose-bom-debug-ui-tooling"))
            androidTestImplementation(libs.getLibrary("compose-bom-debug-ui-test-manifest"))
            implementation(libs.getBundle("compose"))

            // orbit 관련 의존성
            implementation(libs.getBundle("orbit"))
            testImplementation(libs.getLibrary("orbit.test"))
        }
    }
}