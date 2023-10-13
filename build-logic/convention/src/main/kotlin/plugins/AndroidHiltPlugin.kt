package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Hilt 를 적용할 모듈에 사용할 플러그인
 *
 * plugin id : [yello.android.androidHilt]
 */
class AndroidHiltPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("dagger.hilt.android.plugin")
            }
        }
    }
}