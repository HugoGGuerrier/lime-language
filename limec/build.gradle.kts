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

application {
    mainClass = "com.limelanguage.MainKt"
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
