pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://jitpack.io")
        google()
        // TODO: REMOVE TESTING PURPOSE ONLY
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "Pandoro"
include(":app")
 