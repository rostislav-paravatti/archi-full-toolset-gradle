pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.6.10"
        id("org.gradle.kotlin.kotlin-dsl") version "2.1.0"
    }
}

rootProject.name = "com.archimatetool.widgets"
