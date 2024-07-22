package plugins

import com.android.build.api.dsl.LibraryExtension
import ext.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Android Library 모듈에 compose를 사용할 경우 적용할 Plugin
 *
 * plugin id : [yello.android.library.compose]
 */
class AndroidLibraryComposePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            configureAndroidCompose(extensions.getByType<LibraryExtension>())
        }
    }
}