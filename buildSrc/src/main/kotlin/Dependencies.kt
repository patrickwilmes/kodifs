/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/

object Plugins {
    const val Protobuf = "com.google.protobuf"
    const val ProtobufVersion = "0.9.4"
    const val KtorPlugin = "io.ktor.plugin"
    const val KtorPluginVersion = "2.3.12"
    const val KotlinxSerialization = "org.jetbrains.kotlin.plugin.serialization"
    const val KotlinxSerializationVersion = "2.0.0"
}
object Dependencies {
    const val KtorContentNegotiation = "io.ktor:ktor-server-content-negotiation-jvm"
    const val KtorCore = "io.ktor:ktor-server-core-jvm"
    const val KtorKotlinxJson = "io.ktor:ktor-serialization-kotlinx-json-jvm"
    const val KtorCors = "io.ktor:ktor-server-cors-jvm"
    const val KtorNetty = "io.ktor:ktor-server-netty-jvm"
    const val LogBack = "ch.qos.logback:logback-classic:1.4.14"
    const val KtorServerConfig = "io.ktor:ktor-server-config-yaml"
    const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC"
}
