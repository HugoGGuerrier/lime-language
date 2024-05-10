plugins {
    kotlin("jvm")
}

group = "com.limelanguage"
version = parent?.version ?: "undefined"

repositories {
    mavenCentral()
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