plugins {
    kotlin("jvm") version "1.7.21"
    application

    // JSON Serialization
    kotlin("plugin.serialization") version "1.7.21"

    // KtorFit
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"

    // SQLdelight
    id("com.squareup.sqldelight") version "1.5.4"
}

group = "es.dam"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // Kotlin Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")
    implementation("ch.qos.logback:logback-classic:1.4.5")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // Ktorfit
    ksp("de.jensklingenberg.ktorfit:ktorfit-ksp:1.0.0-beta16")
    implementation("de.jensklingenberg.ktorfit:ktorfit-lib:1.0.0-beta16")

    // Ktor JSON Serialization
    implementation("io.ktor:ktor-client-serialization:2.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")

    // SqlDeLight
    implementation("com.squareup.sqldelight:runtime:1.5.4")

    // SQLite
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")

    // Corrutinas SqlDeLight
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.4")

    // KMongo Asíncrono
    implementation("org.litote.kmongo:kmongo-async:4.7.2")

    // KMongo Síncrono
    implementation("org.litote.kmongo:kmongo:4.7.2")

    // Terminal
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta8")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

// Código de SqlDeLight generado al buildear
buildscript {
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.4")
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "database"
    }
}