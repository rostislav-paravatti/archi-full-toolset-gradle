//import org.gradle.internal.jvm.Jvm
import org.gradle.internal.os.OperatingSystem
//import java.util.Properties

plugins {
    java
    `java-library`
    kotlin("jvm") version "1.6.10"
    //`kotlin-stdlib`
    //`kotlin-dsl-precompiled-script-plugins`
    //id("buildlogic.java-conventions")
}

description = "com.archimatetool.canvas"

val lwjglVersion = "3.2.2"
val swtVersion = "3.126.0"

tasks.named<Copy>("processResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

val packageSources = tasks.register<Jar>("packageSources") {
    //duplicatesStrategy = DuplicatesStrategy.INCLUDE
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

artifacts {
    add("archives", packageSources)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    sourceSets {
        getByName("main") {
            java.srcDirs("src/com/archimatetool/canvas/") // Укажите путь к вашим исходникам
            resources.srcDirs("src/main/resources") // путь к ресурсам
        }
    }
}

//sourceSets["main"].java.srcDir("src")

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

tasks.getting(Tar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Jar> { duplicatesStrategy = DuplicatesStrategy.INHERIT }


tasks.withType<JavaCompile> {
    destinationDirectory.set(file("$buildDir/bin")) // Укажите директорию для скомпилированных классов
}

dependencies {
    implementation("org.eclipse.jgit:org.eclipse.jgit:+")
    implementation("org.eclipse.platform:org.eclipse.swt:+")
    implementation("org.eclipse.platform:org.eclipse.equinox.security:+")
    implementation("org.eclipse.platform:org.eclipse.jface:+")
    implementation("com.liferay:org.jdom2:+")
    implementation("org.eclipse.platform:org.eclipse.ui:+")
    implementation(project(":com.archimatetool.editor")) 
    implementation(project(":com.archimatetool.model"))
    implementation(project(":com.archimatetool.templates"))
    //api("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin:+")
    //compileOnly("org.jetbrains.kotlin:kotlin-script-runtime:+")
    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin:kotlin-script-runtime:+")
    //implementation("org.eclipse.platform:org.eclipse.core.runtime:+")
    //implementation("org.eclipse.emf:org.eclipse.emf.ecore:+")
    //implementation("org.eclipse.emf:org.eclipse.emf.ecore.xmi:+")
    //implementation("org.jetbrains.kotlin:kotlin-scripting-jvm:+")
    //implementation("org.jetbrains.kotlin:kotlin-script-runtime:+")
    //implementation("org.jdom:jdom2:+")
    //implementation(files("/kotlin-script-runtime-2.0.0.jar"))
 implementation(project(":org.eclipse.gef"))
 implementation(project(":org.eclipse.draw2d"))
 implementation(project(":com.archimatetool.jdom"))
implementation("org.eclipse.platform:org.eclipse.ui.views.properties.tabbed:+")
}

//dependencies {
//        implementation(files("/kotlin-script-runtime-2.0.0.jar"))
//}

//repositories {
//    mavenCentral()
//    gradlePluginPortal()  // Это важно для доступа к Gradle Plugin Repository
//    maven {
//        url = uri("https://repo.maven.apache.org/maven2")
//    }
//}
