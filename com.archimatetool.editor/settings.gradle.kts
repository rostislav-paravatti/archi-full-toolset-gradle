//rootProject.name = "com.archimatetool.editor"

include(":com.archimatetool.model")
project(":com.archimatetool.model").projectDir = file("../com.archimatetool.model")

include(":com.archimatetool.modelrepository.commandline")
project(":com.archimatetool.modelrepository.commandline").projectDir = file("./com.archimatetool.modelrepository.commandline")




include(":com.archimatetool.model")
project(":com.archimatetool.model").projectDir = file("../com.archimatetool.model")


include(":com.archimatetool.jdom")

project(":com.archimatetool.jdom").projectDir = file("../com.archimatetool.jdom")


pluginManagement {

    includeBuild("./com.archimatetool.model")
    includeBuild("./com.archimatetool.jdom")
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("org.jetbrains.kotlin.jvm") version "1.6.10"
        id("org.gradle.kotlin.kotlin-dsl") version "2.1.0"
    }
}

