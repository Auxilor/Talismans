pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.jpenilla.xyz/snapshots/")
        maven("https://jitpack.io")
    }
}

rootProject.name = "Talismans"

// Core
include(":eco-core")
include(":eco-core:core-plugin")
