/**
 * This is the Gradle build spec for the Lime language compiler. This subproject contains all what you need to compile
 * Lime sources to LLVM bitcode.
 *
 * @author Hugo Guerrier
 */

plugins {
    kotlin("jvm")
    application
}

dependencies {
    // Dependency on the Lime frontend library
    implementation(project(":liblimelang"))

    // Mordant, an output styling library
    implementation("com.github.ajalt.mordant:mordant:2.7.2")
    implementation("com.github.ajalt.mordant:mordant-coroutines:2.7.2")

    // Test dependencies
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Define the main class of the Lime compilers
application {
    mainClass = "com.limelanguage.MainKt"
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
