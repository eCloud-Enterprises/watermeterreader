pluginManagement {
    includeBuild("build-logic")
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
    }
}
rootProject.name = "watermeterreader"
include(":app")
include(":core:designsystem")
include(":core:ui")
include(":core:common")
include(":core:data")
include(":core:data-test")
include(":core:database")
include(":core:datastore")
include(":core:datastore-test")
include(":core:model")
include(":core:network")
include(":core:testing")
include(":core:ui")
include(":feature:onboarding")
include(":sync:work")
include(":feature:auth")
include(":feature:reader")
include(":feature:upload")
include(":feature:projects")
include(":feature:settings")
