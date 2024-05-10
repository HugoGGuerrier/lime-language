plugins {
    kotlin("jvm")
    application
}

group = "com.limelanguage"
version = parent?.version ?: "undefined"

repositories {
    mavenCentral()
}

dependencies {
    // Dependency on the Lime frontend library
    implementation(project(":liblimelang"))

    // Test dependencies
    testImplementation(kotlin("test"))
}

application {
    mainClass = "com.limelanguage.MainKt"
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}