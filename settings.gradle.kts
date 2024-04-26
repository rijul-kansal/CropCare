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
        google()
        mavenCentral()
            maven ( url = "https://kommunicate.jfrog.io/artifactory/kommunicate-android-sdk" )
    }
}

rootProject.name = "Crop Care"
include(":app")
