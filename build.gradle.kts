description = "Hackathon api"

val graphglueVersion: String by project
val graphqlJavaVersion: String by project
val javaVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(javaVersion.toInt())
}

dependencies {
    api("io.github.graphglue", "graphglue", graphglueVersion)
    api("com.graphql-java","graphql-java-extended-scalars", graphqlJavaVersion)
    implementation("com.azure", "azure-ai-formrecognizer", "4.0.4")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.5.1")
    implementation("com.theokanning.openai-gpt3-java", "service", "0.12.0")
}