pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "RentPlatform"
include(":app")
include(":core:common")
include(":core:ui")
include(":core:network")
include(":feature:auth")
include(":feature:marketplace")
include(":feature:profile")
include(":core:session")
include(":feature:favorites")
include(":feature:deals")
