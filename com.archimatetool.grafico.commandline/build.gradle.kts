import org.gradle.internal.os.OperatingSystem
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.archimatetool"
version = "5.4.0"

description = "com.archimatetool.grafico.commandline"

val lwjglVersion = "3.2.2"
val swtVersion = "3.126.0"


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val packageSources = tasks.register<Jar>("packageSources") {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    add("archives", packageSources)
}


tasks {
    processResources {
        from("src/main/resources") {
            include("**/*.properties")
        }
    }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    sourceSets {
        getByName("main") {
            java.srcDirs("src/com/archimatetool/grafico/commandline/") // Укажите путь к вашим исходникам
            resources.srcDirs("src/main/resources") // путь к ресурсам
        }
    }
}

tasks.withType<Jar> {
    manifest {
        from("$projectDir/META-INF/MANIFEST.MF")
    }
}

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

// com.archimatetool.grafico,
// com.archimatetool.commandline

dependencies {
    implementation("org.eclipse.platform:org.eclipse.osgi.util:+")
    implementation("org.eclipse.platform:org.eclipse.equinox.app:+")
    implementation("org.eclipse.platform:org.eclipse.core.runtime:+")

    //implementation(files("lib/commons-cli-1.4-sources.jar"))
    //implementation(files("lib/commons-cli-1.4.jar"))
    //implementation("p2.osgi.bundle:org.apache.commons.cli:+")
    //implementation("org.apache.commons:org.apache.commons.cli:+")
    implementation("org.eclipse.jgit:org.eclipse.jgit:+")
    implementation("org.eclipse.platform:org.eclipse.swt:+")
    implementation("org.eclipse.platform:org.eclipse.equinox.security:+")
    implementation("org.eclipse.platform:org.eclipse.jface:+")
    implementation("com.liferay:org.jdom2:+")
    implementation("org.eclipse.platform:org.eclipse.ui:+")
    implementation(project(":com.archimatetool.commandline"))
    implementation(project(":com.archimatetool.grafico"))
    implementation(project(":com.archimatetool.model"))
    implementation(project(":com.archimatetool.editor"))
    implementation(files("lib/commons-cli-1.4-sources.jar"))
    implementation(files("lib/commons-cli-1.4.jar"))
    // implementation(project(":com.archimatetool.templates"))
    //api("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:+")
    //compileOnly("org.jetbrains.kotlin:kotlin-script-runtime:+")
    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin:kotlin-script-runtime:+")
    //implementation("org.eclipse.platform:org.eclipse.core.runtime:+")
    implementation("org.eclipse.emf:org.eclipse.emf.ecore:+")
    //implementation("org.eclipse.emf:org.eclipse.emf.ecore.xmi:+")
    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin:kotlin-script-runtime:+")
    //implementation("org.jdom:jdom2:+")
implementation(files("lib/commons-csv-1.6.jar"))
    //implementation(files("/kotlin-script-runtime-2.0.0.jar"))
 implementation(project(":org.eclipse.gef"))
 implementation(project(":org.eclipse.draw2d"))
 implementation(project(":com.archimatetool.jdom"))
implementation("org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:+")
}
