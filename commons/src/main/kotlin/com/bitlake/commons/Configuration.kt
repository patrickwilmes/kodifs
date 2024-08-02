package com.bitlake.commons

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

sealed class Value {
    data object NoValue : Value()
    data class StringValue(val value: String) : Value()
}

fun <K> applyForConfigTriple(
    noOpAction: () -> K,
    opAction: (String, String, String) -> K,
    value1: Value,
    value2: Value,
    value3: Value,
): K {
    val valueNotDetected = listOf(value1, value2, value3).any { it is Value.NoValue }
    return if (valueNotDetected) {
        noOpAction()
    } else {
        opAction(
            (value1 as Value.StringValue).value,
            (value2 as Value.StringValue).value,
            (value3 as Value.StringValue).value,
        )
    }
}

fun Application.configValue(configurationValue: ConfigurationValue): Value {
    val configValue = environment.config.propertyOrNull(configurationValue.key)
    return if (configValue == null) Value.NoValue else Value.StringValue(configValue.getString())
}

fun Application.stringConfigValue(configurationValue: ConfigurationValue): String =
    when (val v = configValue(configurationValue)) {
        Value.NoValue -> ""
        is Value.StringValue -> v.value
    }

fun Application.booleanConfigValue(configurationValue: ConfigurationValue): Boolean =
    when (configValue(configurationValue)) {
        Value.NoValue -> false
        is Value.StringValue -> configurationValue.key.toBoolean()
    }

fun Application.intConfigValue(configurationValue: ConfigurationValue): Int =
    when (val v = configValue(configurationValue)) {
        Value.NoValue -> 0
        is Value.StringValue -> v.value.toInt()
    }

sealed class ConfigurationValue(val key: String) {
    data object DatabaseUrl : ConfigurationValue("database.url")
    data object DatabaseUser : ConfigurationValue("database.username")
    data object DatabasePassword : ConfigurationValue("database.password")
    data object DevelopmentMode : ConfigurationValue("ktor.development")
    data object PulsarUrl : ConfigurationValue("broker.url")
    data object HeartbeatIntervalSeconds : ConfigurationValue("system.heartbeatIntervalSeconds")
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.configure(isDevelopmentMode: Boolean = false) {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        if (isDevelopmentMode) {
            anyHost()
        }
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                explicitNulls = false
            },
        )
    }
}
