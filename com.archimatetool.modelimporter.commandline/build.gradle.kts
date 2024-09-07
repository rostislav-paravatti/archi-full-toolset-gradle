import org.gradle.internal.os.OperatingSystem
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}
group = "com.archimatetool"
version = "5.4.0"

val lwjglVersion = "3.2.2"
val swtVersion = "3.126.0"

description = "com.archimatetool.modelimporter.commandline"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2")
    }
    maven {
        url = uri("https://jcenter.bintray.com/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/releases/")
    }
    maven {
        url = uri("https://repo.spring.io/release")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
    maven {
        url = uri("https://maven.google.com")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/groups/releases/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/groups/build/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/groups/snapshots/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/groups/staging/")
    }
    maven {
        url = uri("https://download.eclipse.org/releases/latest/")
    }
    maven {
        url = uri("https://download.eclipse.org/egit/github/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/repositories/egit-releases/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/repositories/egit-snapshots/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/repositories/egit-staging/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/repositories/jgit-releases/")
    }
    maven {
        url = uri("https://repo.eclipse.org/content/repositories/jgit-snapshots/")
    }
}


val currentOs = OperatingSystem.current().familyName.replace(" ", "").lowercase()

val osgiPlatform = when {
    currentOs.contains("win") -> "org.eclipse.swt.win32.win32.x86_64"
    currentOs.contains("linux") -> "org.eclipse.swt.gtk.linux.x86_64"
    currentOs.contains("mac") -> "org.eclipse.swt.cocoa.macosx.x86_64"
    else -> throw GradleException("Unsupported OS: $currentOs")
}

configurations.all {
    resolutionStrategy {
        dependencySubstitution {
            val os = System.getProperty("os.name").lowercase()
            when {
                os.contains("windows") -> substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}"))
                    .because("Resolve SWT for Windows")
                    .using(module("org.eclipse.platform:org.eclipse.swt.win32.win32.x86_64:$swtVersion"))

                os.contains("linux") -> substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}"))
                    .because("Resolve SWT for Linux")
                    .using(module("org.eclipse.platform:org.eclipse.swt.gtk.linux.x86_64:$swtVersion"))

                os.contains("mac") -> substitute(module("org.eclipse.platform:org.eclipse.swt.\${osgi.platform}"))
                    .because("Resolve SWT for Mac")
                    .using(module("org.eclipse.platform:org.eclipse.swt.cocoa.macosx.x86_64:$swtVersion"))
            }
        }
    }
}


dependencies {
    implementation("org.eclipse.tycho:tycho-p2-resolver:+")
    implementation("org.eclipse.jgit:org.eclipse.jgit:+")
    implementation("org.eclipse.platform:org.eclipse.swt:+")
    implementation("org.eclipse.platform:org.eclipse.equinox.security:+")
    implementation("org.eclipse.platform:org.eclipse.jface:+")
    implementation("com.liferay:org.jdom2:+")
    implementation("org.eclipse.platform:org.eclipse.ui:+")
 implementation(project(":com.archimatetool.editor"))
 implementation(project(":com.archimatetool.jasperreports"))
 implementation(project(":com.archimatetool.model"))
 implementation(project(":com.archimatetool.modelimporter"))
 implementation(project(":org.eclipse.gef"))
 implementation(project(":org.eclipse.draw2d"))
 implementation(project(":com.archimatetool.jdom"))
 implementation(project(":com.archimatetool.commandline"))
 implementation("org.eclipse.platform:org.eclipse.equinox.p2.metadata:+")
  implementation("org.eclipse.platform:org.eclipse.equinox.p2.repository:+")
  implementation("org.eclipse.platform:org.eclipse.equinox.p2.core:+")
  implementation("org.eclipse.platform:org.eclipse.equinox.p2.operations:+")
  implementation("org.eclipse.platform:org.eclipse.equinox.p2.engine:+")
  implementation("org.eclipse.platform:org.eclipse.equinox.p2.garbagecollector:+")
  implementation("org.eclipse.platform:org.eclipse.ui.views:+")
  implementation("org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:+")
    implementation(files("lib/commons-cli-1.4-sources.jar"))
    implementation(files("lib/commons-cli-1.4.jar"))
}
