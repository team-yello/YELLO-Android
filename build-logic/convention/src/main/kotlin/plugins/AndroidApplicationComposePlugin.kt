package plugins

import com.android.build.api.dsl.ApplicationExtension
import ext.androidTestImplementation
import ext.configureAndroidCompose
import ext.debugImplementation
import ext.getBundle
import ext.getLibrary
import ext.getVersionCatalog
import ext.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

/**
 * Android Application 모듈에 compose를 사용할 경우 적용할 Plugin
 *
 * plugin id : [yello.android.application.compose]
 */
class AndroidApplicationComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureAndroidCompose(extensions.getByType<ApplicationExtension>())
        }
    }
}
