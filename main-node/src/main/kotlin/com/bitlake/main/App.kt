/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main

import com.bitlake.commons.ConfigurationValue
import com.bitlake.commons.GlobalKoinContext
import com.bitlake.commons.HEARTBEAT_TOPIC
import com.bitlake.commons.Value
import com.bitlake.commons.booleanConfigValue
import com.bitlake.commons.configValue
import com.bitlake.commons.configure
import com.bitlake.commons.pulsarClient
import com.bitlake.commons.runServer
import com.bitlake.main.consumer.Consumer
import com.bitlake.main.consumer.HeartbeatConsumer
import com.bitlake.main.heartbeat.heartbeatModule
import com.bitlake.main.heartbeat.setupHeartbeatJob
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import java.util.UUID
import kotlin.reflect.full.createInstance

@OptIn(DelicateCoroutinesApi::class)
private fun Application.createConsumerForTopic(consumer: Consumer): Job = GlobalScope.launch(Dispatchers.IO) {
    val client =
        pulsarClient((configValue(ConfigurationValue.PulsarUrl) as Value.StringValue).value)

    val pulsarConsumer = client.newConsumer()
        .topic(consumer.topic)
        .subscriptionName("${consumer.topic}-${UUID.randomUUID()}")
        .subscribe()

    try {
        while (true) {
            val msg = pulsarConsumer.receive()
            consumer.consume(msg.value).onRight {
                pulsarConsumer.acknowledge(msg)
            }.onLeft {
                pulsarConsumer.negativeAcknowledge(msg)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        pulsarConsumer.close()
        client.close()
    }
}

fun main() {
    runServer {
        // pulsar topics
        val jobs = listOf(HeartbeatConsumer)
            .map { consumer -> createConsumerForTopic(consumer) }
        configure(isDevelopmentMode = booleanConfigValue(ConfigurationValue.DevelopmentMode))
        install(Koin) {
            modules(heartbeatModule)

            GlobalKoinContext.koin = koin
        }
        connectToDatabase()
        executeFlywayMigration()
        installRoutes()
        setupHeartbeatJob()

        environment.monitor.subscribe(ApplicationStopped) {
            jobs.forEach { it.cancel() }
        }
    }
}

private fun Application.installRoutes() {
    routing {
        get("/hello") {
            call.respond("Hello World!")
        }
    }
}
