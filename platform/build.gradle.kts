plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("plugin.serialization") version "2.3.0"
}

group = "com.xapps"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    // ---- DEPENDS ON CONTRACTS ----
    implementation(project(":contracts"))

    // ---- SERIALIZATION (CLIENT SIDE) ----
    val kotlinSerialization = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinSerialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinSerialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:$kotlinSerialization")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$kotlinSerialization")

    // Ktor
    val ktor = "3.3.3" // formerly 2.3.12
    implementation("io.ktor:ktor-client-core:$ktor")
    implementation("io.ktor:ktor-client-cio:$ktor")
    implementation("io.ktor:ktor-network:${ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor")
    implementation("io.ktor:ktor-client-logging:$ktor")


    // OkHttp
    val okHttpVersion = "5.3.2"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")

    // ---- CACHE ----
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // ---- UTIL ----
    implementation("org.jetbrains.kotlinx:atomicfu:0.24.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.benasher44:uuid-jvm:0.8.4")


    /* =========================================================
     * OCR
     * ========================================================= */
    implementation("net.java.dev.jna:jna:5.13.0")
    implementation("net.sourceforge.tess4j:tess4j:5.9.0")


//    implementation(project(":common:file_manager"))

}

tasks.test {
    useJUnitPlatform()
}
