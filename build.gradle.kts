plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "me.k"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.20-RC")
    implementation(group = "com.azure", name = "azure-ai-formrecognizer", version = "4.0.4")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}