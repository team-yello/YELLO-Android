package ext

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.provider.Provider

fun VersionCatalog.getBundle(bundleName: String): Provider<ExternalModuleDependencyBundle> {
    return findBundle(bundleName).get()
}

fun VersionCatalog.getLibrary(libraryName: String): Provider<MinimalExternalModuleDependency> {
    return findLibrary(libraryName).get()
}
