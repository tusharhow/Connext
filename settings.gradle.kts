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
        maven {
            url = uri("http://jitpack.io")
            isAllowInsecureProtocol = true
        }
    }
}

rootProject.name = "ComposeConnext"
include(":app")
include(":connext")
