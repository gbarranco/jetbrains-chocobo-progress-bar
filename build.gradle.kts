import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType

plugins {
    kotlin("jvm") version "2.4.0"
    id("org.jetbrains.intellij.platform") version "2.20.0"
}

group = "dev.chocobo"
version = "1.1.3"

repositories {
    mavenCentral()
    intellijPlatform { defaultRepositories() }
}

dependencies {
    intellijPlatform {
        intellijIdea("2026.2.1")
        bundledPlugin("com.intellij.java") // Compile-time only; optional at runtime via plugin.xml
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

/**
 * Run the plugin in sandboxed JetBrains IDEs without publishing it.
 * For example: ./gradlew runWebStorm
 */
intellijPlatformTesting {
    runIde {
        register("runPyCharm") {
            type = IntelliJPlatformType.PyCharmProfessional
            version = "2026.2"
        }
        register("runWebStorm") {
            type = IntelliJPlatformType.WebStorm
            version = "2026.2"
        }
        register("runPhpStorm") {
            type = IntelliJPlatformType.PhpStorm
            version = "2026.2"
        }
        register("runGoLand") {
            type = IntelliJPlatformType.GoLand
            version = "2026.2"
        }
    }
}
