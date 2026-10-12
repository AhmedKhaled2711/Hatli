pluginManagement {
    includeBuild("build-logic")
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

rootProject.name = "Hatli"

include(":app")

include(":core:common")
include(":core:ui")
include(":core:designsystem")
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:database")
include(":core:model")

include(":feature:home")
include(":feature:auth")
include(":feature:profile")
include(":feature:request")
include(":feature:chat")
