import org.gradle.internal.os.OperatingSystem
plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
  id("com.github.johnrengelman.shadow") version "7.1.2"
}
group = "com.archimatetool"
version = "5.4.0"

description = "com.archimatetool.modelrepository"

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
            java.srcDirs("src/com/archimatetool/modelrepository/") // Укажите путь к вашим исходникам
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
    implementation(project(":com.archimatetool.model"))
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
 implementation(project(":org.eclipse.gef"))
 implementation(project(":org.eclipse.draw2d"))
 implementation(project(":com.archimatetool.jdom"))
implementation("org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:+")
//implementation("org.eclipse.emf:org.eclipse.emf.compare:+")
//implementation("org.eclipse.emf:org.eclipse.emf.compare:+")

    implementation(files("lib/org.eclipse.emf.compare.diagram.edit.source_2.5.2.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.diagram.edit_2.5.2.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.diagram.gmf.source_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.diagram.gmf_3.3.25.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.diagram.ide.ui.source_3.4.3.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.diagram.ide.ui_3.4.3.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.diagram.source_2.5.2.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.diagram_2.5.2.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.doc_3.3.25.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.edit.source_4.3.1.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.edit_4.3.1.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.egit.source_1.2.4.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.egit.source_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.egit_1.2.4.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.egit_3.3.25.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.egit.ui.source_1.1.3.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.egit.ui_1.1.3.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.ide.source_3.4.3.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.ide_3.4.3.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.ide.ui.e4.source_1.1.0.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.ide.ui.e4.source_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.ide.ui.e4_1.1.0.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.ide.ui.e4_3.3.25.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.ide.ui.source_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.ide.ui.source_4.4.3.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.ide.ui_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.ide.ui_4.4.3.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.rcp.source_2.5.2.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.rcp_2.5.2.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.rcp.ui.source_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.rcp.ui.source_4.4.2.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.rcp.ui_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.rcp.ui_4.4.2.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.source_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.source_3.5.3.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.uml2.edit.source_2.5.2.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2.edit_2.5.2.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2.ide.source_3.3.1.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.uml2.ide_3.3.1.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2.rcp.source_2.5.1.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2.rcp.ui.source_2.6.1.202406060900.jar"))
    
    implementation(files("lib/org.eclipse.emf.compare.uml2.rcp.ui_2.6.1.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2.rcp_2.5.1.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2.source_2.6.1.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2.source_3.3.25.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare.uml2_2.6.1.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare.uml2_3.3.25.202406060900.jar"))

    implementation(files("lib/org.eclipse.emf.compare_3.3.25.202406060900.jar"))
    implementation(files("lib/org.eclipse.emf.compare_3.5.3.202406060900.jar"))
implementation("org.eclipse.platform:org.eclipse.help.ui:+")
implementation("org.eclipse.platform:org.eclipse.equinox.security:+")
implementation("com.google.api-client:google-api-client:+")
implementation("com.google.oauth-client:google-oauth-client-jetty:+")
implementation("com.google.apis:google-api-services-drive:+")
implementation("org.eclipse.jgit:org.eclipse.jgit.ssh.apache:+")
//implementation("org.eclipse.jgit.ssh.apache:org.eclipse.jgit.transport.sshd:+")
}
