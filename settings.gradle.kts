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
        maven("https://repo.clojars.org")
        google()
        mavenLocal()
        mavenCentral()
    }
}

rootProject.name = "Pandoro"
include(":app")
 