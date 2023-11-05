package plugins

import ext.getBundle
import ext.getVersionCatalog
import ext.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * kotlin 를 적용할 모듈에 사용할 플러그인
 *
 * plugin id : [yello.android.kotlin]
 */
class AndroidKotlinPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("kotlin-android")
        }

        val libs = extensions.getVersionCatalog()
        dependencies {
            implementation(libs.getBundle("kotlin"))
        }
    }
}
