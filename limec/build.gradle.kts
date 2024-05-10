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

    // Test dependencies
    testImplementation(kotlin("test"))
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
