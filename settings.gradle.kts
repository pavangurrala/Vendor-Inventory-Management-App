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
        maven { url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials {
                username = "pavang"
                password = "pk.eyJ1IjoicGF2YW5nIiwiYSI6ImNtYXNmcTJ5NTBndncyanM5c3p6ajY0NzkifQ.8mkkNFxEiFHJ6O3-vjGe1w"
            }
        }
    }
}

rootProject.name = "VendorInventoryManagement"
include(":app")
 