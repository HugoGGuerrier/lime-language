/**
 * This is the Gradle build spec for the Lime language general usage frontend. This subproject is used as a library by
 * other lime language components.
 *
 * @author Hugo Guerrier
 */

plugins {
    kotlin("jvm")
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
