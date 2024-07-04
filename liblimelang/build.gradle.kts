import com.strumenta.antlrkotlin.gradle.AntlrKotlinTask
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import kotlin.script.experimental.jvm.util.hasParentNamed

/**
 * This is the Gradle build spec for the Lime language general usage frontend. This subproject is used as a library by
 * other lime language components.
 *
 * @author Hugo Guerrier
 */

plugins {
    // Kotlin compiler
    kotlin("jvm")

    // ANTLR Kotlin target and runtime
    id("com.strumenta.antlr-kotlin") version "1.0.0-RC3"
}

// Configure the kotlin plugin
kotlin {
    jvmToolchain(17)
    sourceSets {
        main {
            kotlin {
                srcDir(layout.buildDirectory.dir("generatedAntlr"))
            }
        }
    }
}

// Project dependencies
dependencies {
    // Kotlin reflection
    implementation(kotlin("reflect"))

    // ANTLR runtime for Kotlin
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC3")

    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.github.java-diff-utils:java-diff-utils:4.12")
}

tasks.test {
    systemProperty("tests.rewriteBaselines", project.properties.getOrDefault("rewrite", "false")!!)
    useJUnitPlatform()
}

// --- ANTLR stuff

// Create a task to generate the grammar sources files
val generateKotlinGrammarSource =
    tasks.register<AntlrKotlinTask>("generateKotlinGrammarSource") {
        dependsOn("cleanGenerateKotlinGrammarSource")

        source =
            fileTree(layout.projectDirectory.dir("src/main/antlr")) {
                include("**/*.g4")
            }

        val pkgName = "com.limelanguage.parser"
        packageName = pkgName

        // We want visitors alongside listeners.
        // The Kotlin target language is implicit, as is the file encoding (UTF-8)
        arguments = listOf("-visitor")

        val outDir = "generatedAntlr/${pkgName.replace(".", "/")}"
        outputDirectory = layout.buildDirectory.dir(outDir).get().asFile
    }

// Generate the grammar sources files for each compilation
tasks.withType<KotlinCompile<*>> {
    dependsOn(generateKotlinGrammarSource)
}

// Ensure that the style-checking task is ran after the grammar generation
tasks.runKtlintCheckOverMainSourceSet {
    dependsOn("generateKotlinGrammarSource")
}

// Exclude the generated sources from the style-checking process
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    filter {
        exclude { element -> element.file.hasParentNamed("generatedAntlr") }
    }
}
