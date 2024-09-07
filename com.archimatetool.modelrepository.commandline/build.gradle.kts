import org.gradle.internal.os.OperatingSystem
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}
group = "com.archimatetool"
version = "5.4.0"


description = "com.archimatetool.modelrepository.commandline"

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

tasks.withType<Jar> {
    manifest {
        from("$projectDir/META-INF/MANIFEST.MF")
    }
}

dependencies {
    implementation(project(":com.archimatetool.commandline"))
    implementation("org.eclipse.platform:org.eclipse.equinox.security:+")
    implementation(project(":com.archimatetool.modelrepository"))
    implementation(project(":com.archimatetool.model"))
    implementation(project(":com.archimatetool.editor"))
    implementation("org.eclipse.jgit:org.eclipse.jgit:+")
    implementation(files("lib/commons-cli-1.4-sources.jar"))
    implementation(files("lib/commons-cli-1.4.jar"))
implementation(files("lib/commons-beanutils-1.9.3.jar"))
implementation(files("lib/commons-collections-3.2.2.jar"))
implementation(files("lib/commons-digester-2.1.jar"))
implementation(files("lib/commons-logging-1.2.jar"))
implementation(files("lib/ecj-4.9.jar"))
implementation(files("lib/itext-2.1.7.js6.jar"))
implementation(files("lib/jackson-core-2.9.8.jar"))
implementation(files("lib/jackson-databind-2.9.8.jar"))
implementation(files("lib/jasperreports-6.7.1.jar"))
implementation(files("lib/jasperreports-fonts-6.7.1.jar"))
    implementation("org.eclipse.emf:org.eclipse.emf.ecore:+")
    implementation("org.eclipse.emf:org.eclipse.emf.common:+")
    implementation("org.eclipse.emf:org.eclipse.emf.ecore.xmi:+")
}

