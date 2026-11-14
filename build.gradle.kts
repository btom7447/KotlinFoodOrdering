// Build configuration for the KotlinFoodOrdering command-line app (Kotlin/JVM).
plugins {
    kotlin("jvm") version "2.4.21"
    application
}

group = "foodordering"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // kotlin-test maps kotlin.test assertions onto JUnit 5; no other libraries are used.
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "foodordering.MainKt"
}

tasks.test {
    useJUnitPlatform()
}

// Lets `./gradlew run` read what the customer types.
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
