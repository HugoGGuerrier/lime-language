import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

// Project-wide plugins
plugins {
    // Kotlin compiler
    kotlin("jvm") version "1.9.23"

    // Kotlin style-checker
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
    additionalEditorconfig.set(
        mapOf(
            "max_line_length" to "100",
            "end_of_line" to "lf",
            "insert_final_newline" to "true",
        ),
    )
    reporters {
        reporter(
            ReporterType.JSON,
        )
    }
}

// Simplify the files formatting by adding a "fmt" task
task("fmt") {
    dependsOn("ktlintFormat")
    dependsOn(":liblimelang:ktlintFormat")
    dependsOn(":limec:ktlintFormat")
}

// Define common settings for subprojects
subprojects {
    // Set their group and version according to the root project
    group = parent?.group ?: "undefined"
    version = parent?.version ?: "undefined"

    // Tell them to retrieve dependencies from the Maven central
    repositories {
        mavenCentral()
    }

    // Apply and configure the source formatter for all subprojects
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.2.1")
        android.set(false)
        additionalEditorconfig.set(
            mapOf(
                "max_line_length" to "100",
                "end_of_line" to "lf",
                "insert_final_newline" to "true",
            ),
        )
        reporters {
            reporter(
                ReporterType.JSON,
            )
        }
    }
}
