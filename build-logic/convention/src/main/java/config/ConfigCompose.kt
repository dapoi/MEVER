package config

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import util.CollectionLibs.commonDependencies

internal fun Project.configCompose(
    extension: Any
) {
    when (extension) {
        is ApplicationExtension -> extension.buildFeatures { compose = true }
        is LibraryExtension -> extension.buildFeatures { compose = true }
    }
    commonDependencies()
}