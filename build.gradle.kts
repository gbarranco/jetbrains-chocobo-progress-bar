import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.0"
    id("org.jetbrains.intellij.platform") version "2.19.0"
}

group = "dev.chocobo"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform { defaultRepositories() }
}

dependencies {
    intellijPlatform { intellijIdea("2026.2.1") }
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
    pluginConfiguration {
        id = "dev.chocobo.progress"
        name = "Chocobo Progress"
        version = project.version.toString()
        ideaVersion {
            sinceBuild = "262"
            untilBuild = "262.*"
        }
    }
}
