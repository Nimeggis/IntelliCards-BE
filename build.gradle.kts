description = "Hackathon api"

val graphglueVersion: String by project
val graphqlJavaVersion: String by project
val javaVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
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
}