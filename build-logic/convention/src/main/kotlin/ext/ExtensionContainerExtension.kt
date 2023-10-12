package ext

import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.getByType

fun ExtensionContainer.getVersionCatalog(): VersionCatalog {
    return getByType<VersionCatalogsExtension>().named("libs")
}
