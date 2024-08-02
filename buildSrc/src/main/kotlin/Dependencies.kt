import org.gradle.api.artifacts.dsl.DependencyHandler

/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/

object Plugins {
    const val KtorPlugin = "io.ktor.plugin"
    const val KtorPluginVersion = "2.3.12"
    const val KotlinxSerialization = "org.jetbrains.kotlin.plugin.serialization"
    const val KotlinxSerializationVersion = "2.0.0"
}

object Dependencies {
    const val koinCore = "io.insert-koin:koin-core:3.5.6"
    const val koinKtor = "io.insert-koin:koin-ktor:3.5.6"
    const val koinSlf4j = "io.insert-koin:koin-logger-slf4j:3.5.6"
    const val KtorContentNegotiation = "io.ktor:ktor-server-content-negotiation-jvm"
    const val KtorCore = "io.ktor:ktor-server-core-jvm"
    const val KtorKotlinxJson = "io.ktor:ktor-serialization-kotlinx-json-jvm"
    const val KtorCors = "io.ktor:ktor-server-cors-jvm"
    const val KtorNetty = "io.ktor:ktor-server-netty-jvm"
    const val LogBack = "ch.qos.logback:logback-classic:1.4.14"
    const val KtorServerConfig = "io.ktor:ktor-server-config-yaml"
    const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC"
    const val PulsarClient = "org.apache.pulsar:pulsar-client:3.3.1"
    const val SuspendApp = "io.arrow-kt:suspendapp:0.4.0"
    const val ArrowKtStack = "io.arrow-kt:arrow-stack:1.2.4"
    const val ArrowKtCore = "io.arrow-kt:arrow-core"
    const val ArrowKtCoroutines = "io.arrow-kt:arrow-fx-coroutines"
    const val exposedCore = "org.jetbrains.exposed:exposed-core:0.53.0"
    const val exposedDao = "org.jetbrains.exposed:exposed-dao:0.53.0"
    const val exposedJdbc = "org.jetbrains.exposed:exposed-jdbc:0.53.0"
    const val exposedKotlinDateTime =
        "org.jetbrains.exposed:exposed-kotlin-datetime:0.53.0"
    const val Postgresql = "org.postgresql:postgresql:42.7.1"
    const val flyway = "org.flywaydb:flyway-core:10.17.0"
    const val FlywayPostgres = "org.flywaydb:flyway-database-postgresql:10.17.0"
    const val Quartz = "org.quartz-scheduler:quartz:2.3.2"
}

fun DependencyHandler.applyDatabaseSupport() {
    add("implementation", Dependencies.exposedDao)
    add("implementation", Dependencies.exposedCore)
    add("implementation", Dependencies.exposedJdbc)
    add("implementation", Dependencies.exposedKotlinDateTime)
    add("implementation", Dependencies.Postgresql)
    add("implementation", Dependencies.flyway)
    add("implementation", Dependencies.FlywayPostgres)
}

fun DependencyHandler.applyKoin() {
    add("implementation", Dependencies.koinKtor)
    add("implementation", Dependencies.koinCore)
    add("implementation", Dependencies.koinSlf4j)
}

fun DependencyHandler.applyArrowStack() {
    add("implementation", platform(Dependencies.ArrowKtStack))
    add("implementation", Dependencies.ArrowKtCore)
    add("implementation", Dependencies.ArrowKtCoroutines)
    add("implementation", Dependencies.SuspendApp)
}
