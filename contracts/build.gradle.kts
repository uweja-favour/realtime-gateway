plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "com.xapps"
version = "0.0.1-SNAPSHOT"
description = "Contracts"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // ---- PUBLIC API TYPES ----
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

    // ---- SERIALIZATION (DTOs) ----
    val kotlinSerialization = "1.7.3"
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialization")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerialization")
    api("org.jetbrains.kotlinx:kotlinx-serialization-cbor:$kotlinSerialization")


    implementation("com.squareup.okio:okio:3.9.0")


    // ---- BASIC UTIL ----
    implementation("org.jetbrains.kotlinx:atomicfu:0.24.0")

//    implementation(project(":common:file_manager"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}
