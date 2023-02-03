import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"

    // JSON Serialization
    kotlin("plugin.serialization") version "1.7.22"

    // KtorFit
    id("com.google.devtools.ksp") version "1.7.21-1.0.8"

    // SQLdelight
    id("com.squareup.sqldelight") version "1.5.4"
}

group = "es.dam"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("io.projectreactor:reactor-test")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")


    // KMongo Asíncrono
    implementation("org.litote.kmongo:kmongo-async:4.7.2")

    // KMongo Síncrono
    implementation("org.litote.kmongo:kmongo:4.7.2")

    // Kotlin Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.3")

    // SqlDeLight
    implementation("com.squareup.sqldelight:runtime:1.5.4")
    // SQLite
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.4")
    // Corrutinas SqlDeLight
    implementation("com.squareup.sqldelight:coroutines-extensions-jvm:1.5.4")

    // Corrutinas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    //Serializar ID KMongo
    implementation("org.litote.kmongo:kmongo-id-serialization:4.1.3")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    // Ktorfit
    ksp("de.jensklingenberg.ktorfit:ktorfit-ksp:1.0.0-beta16")
    implementation("de.jensklingenberg.ktorfit:ktorfit-lib:1.0.0-beta16")

    // Ktor JSON Serialization
    implementation("io.ktor:ktor-client-serialization:2.1.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.3")

    //Codificación contraseñas
    implementation("com.google.guava:guava:31.1-jre")

    // Terminal
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta8")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.4")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        // Desactivamos el mockito-core para usar mockk
        exclude(module = "mockito-core")
    }
    testImplementation("com.ninja-squad:springmockk:4.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
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