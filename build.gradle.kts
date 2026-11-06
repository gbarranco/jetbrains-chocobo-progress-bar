import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    kotlin("jvm") version "2.4.0"
    id("org.jetbrains.intellij.platform") version "2.20.0"
}

group = "dev.chocobo"
version = "1.1.1"

repositories {
    mavenCentral()
    intellijPlatform { defaultRepositories() }
}

dependencies {
    intellijPlatform {
        intellijIdea("2026.2.1")
        bundledPlugin("com.intellij.java")
        bundledModule("intellij.platform.smRunner")
    }
}

kotlin {
    jvmToolchain(24)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_24)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(24)
}

intellijPlatform {
    pluginVerification {
        ides {
            current()
            create(IntelliJPlatformType.IntellijIdea, "263.7307.112")
        }
    }
    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
        channels = listOf("default")
    }
    pluginConfiguration {
        id = "dev.chocobo.progress"
        name = "Chocobo Progress"
        version = project.version.toString()
        ideaVersion {
            sinceBuild = "262"
            untilBuild = "263.*"
        }
    }
}
