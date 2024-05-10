import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Project-wide plugins
plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "com.limelanguage"
version = "0.1"

// We want dependencies to be resolved from the Maven central
repositories {
    mavenCentral()
}

// Configuration of the formatting plugin for the root project
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    version.set("1.2.1")
    android.set(false)
    reporters {
        reporter(ReporterType.JSON)
    }
}

// Define common settings for subprojects
subprojects {
    group = "com.limelanguage"
    version = "0.1"

    repositories {
        mavenCentral()
    }

    // Apply and configure the source formatter for all subprojects
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.2.1")
        android.set(false)
        reporters {
            reporter(ReporterType.JSON)
        }
    }
}

// Simplify the files formatting
task("fmt") {
    dependsOn("ktlintFormat")
}
