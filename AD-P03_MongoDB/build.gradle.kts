// Ktor y Kotlin
val ktor_version: String by project
val kotlin_version: String by project

// Logger
// val logback_version: String by project
val micrologging_version: String by project
val logbackclassic_version: String by project

// Cache
val cache_version: String by project

// Test
val junit_version: String by project
val mockk_version: String by project
val coroutines_version: String by project

// Koin
val koin_ktor_version: String by project
val ksp_version: String by project
val koin_ksp_version: String by project
val koin_version: String by project

//Util
val sqldelight_version: String by project
val mongodb_version: String by project
val ktorfit_version: String by project

plugins {
    kotlin("jvm") version "1.7.22"
    application

    // JSON Serialization
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.22"

    // KtorFit
    id("com.google.devtools.ksp") version "1.8.0-1.0.8"

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
    implementation("io.github.microutils:kotlin-logging-jvm:$micrologging_version")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // Ktorfit
    ksp("de.jensklingenberg.ktorfit:ktorfit-ksp:$ktorfit_version")
    implementation("de.jensklingenberg.ktorfit:ktorfit-lib:$ktorfit_version")

    // Ktor JSON Serialization
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    // SqlDeLight
    implementation("com.squareup.sqldelight:runtime:$sqldelight_version")

    // SQLite
    implementation("com.squareup.sqldelight:sqlite-driver:$sqldelight_version")

    // Corrutinas SqlDeLight
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:$sqldelight_version")

    // KMongo Asíncrono
    implementation("org.litote.kmongo:kmongo-async:$mongodb_version")

    // KMongo Síncrono
    implementation("org.litote.kmongo:kmongo:$mongodb_version")

    // Terminal
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta8")

    //Codificación contraseñas
    implementation("com.google.guava:guava:31.1-jre")

    // Koin - Core (con esto ya va Koin en modo normal!!
    implementation("io.insert-koin:koin-core:$koin_version")

    // Si queremos usar Koin en modo Annotations
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")

    // JUnit 5 en vez del por defecto de Kotlin...
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit_version")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junit_version")

    // MockK para testear Mockito con Kotlin
    testImplementation("io.mockk:mockk:$mockk_version")

    // Para testear métodos suspendidos o corrutinas
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_version")

    // Para testear con content negotiation
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    // Auth para tokens usando el metodo de clienteAuth
    implementation("io.ktor:ktor-client-auth:$ktor_version")

    // JSON content negotiation
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    // Content validation
    implementation("io.ktor:ktor-server-request-validation:$ktor_version")


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

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}