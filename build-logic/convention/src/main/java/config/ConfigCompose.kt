package config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import util.CollectionLibs.commonDependencies
import org.gradle.api.Project

internal fun Project.configCompose(
    extension: Any
) {
    when (extension) {
        is ApplicationExtension -> extension.buildFeatures { compose = true }
        is LibraryExtension -> extension.buildFeatures { compose = true }
    }
    commonDependencies()
}