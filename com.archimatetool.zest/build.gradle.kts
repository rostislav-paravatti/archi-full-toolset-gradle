import org.gradle.internal.os.OperatingSystem
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}
group = "com.archimatetool"
version = "5.4.0"

description = "com.archimatetool.zest"

val lwjglVersion = "3.2.2"
val swtVersion = "3.126.0"

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
    //implementation("org.eclipse.emf:org.eclipse.emf.ecore:+")
    //implementation("org.eclipse.emf:org.eclipse.emf.common:+")
    //implementation("org.eclipse.emf:org.eclipse.emf.ecore.xmi:+")
    implementation("org.eclipse.jgit:org.eclipse.jgit:+")
    implementation("org.eclipse.platform:org.eclipse.swt:+")
    implementation("org.eclipse.platform:org.eclipse.equinox.security:+")
    implementation("org.eclipse.platform:org.eclipse.jface:+")
    implementation("com.liferay:org.jdom2:+")
    implementation("org.eclipse.platform:org.eclipse.ui:+")
    implementation(project(":com.archimatetool.editor"))
    implementation(project(":com.archimatetool.editor.browser"))
    implementation(project(":com.archimatetool.model"))
    implementation(project(":com.archimatetool.canvas"))
    implementation(project(":com.archimatetool.help"))
    implementation("org.eclipse.platform:org.eclipse.osgi.util:+")
    implementation("org.eclipse.platform:org.eclipse.equinox.app:+")
    implementation("org.eclipse.platform:org.eclipse.core.runtime:+")
    implementation("org.eclipse.jgit:org.eclipse.jgit:+")
    implementation("org.eclipse.platform:org.eclipse.swt:+")
    implementation("org.eclipse.platform:org.eclipse.equinox.security:+")
    implementation("org.eclipse.platform:org.eclipse.jface:+")
    implementation("com.liferay:org.jdom2:+")
    implementation("org.eclipse.platform:org.eclipse.ui:+")
    implementation(project(":com.archimatetool.editor"))
    implementation(project(":com.archimatetool.model"))
    implementation(project(":com.archimatetool.templates"))
    //implementation("org.eclipse.emf:org.eclipse.emf.ecore:+")
    //implementation(files("lib/org.eclipse.emf.compare.source_3.3.25.jar"))
    //implementation(files("lib/org.eclipse.emf.compare_3.3.25.jar"))
    //implementation(files("lib/org.eclipse.zest.layouts-1.1.100.201408150207.jar"))    
    //implementation(files("lib/org.eclipse.zest.core-3.4.0.v20140429-1120.jar"))
 implementation(project(":org.eclipse.zest.core"))
 implementation(project(":org.eclipse.zest.layouts"))
 implementation(project(":org.eclipse.gef"))
 implementation(project(":org.eclipse.draw2d"))
 implementation(project(":com.archimatetool.jdom"))
implementation("org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:+")
//implementation("org.eclipse.emf:org.eclipse.emf.compare:+")
//implementation("org.eclipse.emf:org.eclipse.emf.compare:+")

implementation("org.eclipse.platform:org.eclipse.help.ui:+")
//implementation("org.eclipse.zest:org.eclipse.zest.core:+")
//implementation("org.eclipse.zest:org.eclipse.zest.layouts:+")
}
