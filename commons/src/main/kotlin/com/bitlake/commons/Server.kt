/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.commons

import arrow.fx.coroutines.Resource
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.host
import io.ktor.server.application.port
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

fun <TEngine : ApplicationEngine, TConfiguration : ApplicationEngine.Configuration> server(
    factory: ApplicationEngineFactory<TEngine, TConfiguration>,
    configure: TConfiguration.() -> Unit = {},
    preWait: Duration = 30.seconds,
    grace: Duration = 1.seconds,
    timeout: Duration = 5.seconds,
): Resource<ApplicationEngine> =
    Resource(
        {
            embeddedServer(
                factory,
                environment = applicationEngineEnvironment {
                    config = HoconApplicationConfig(ConfigFactory.load())
                    connector {
                        port = config.port
                        host = config.host
                    }
                    watchPaths = listOf("classes")
                },
                configure = configure,
            ).apply { start() }
        },
        { engine, _ ->
            if (!engine.environment.developmentMode) {
                engine.environment.log.info(
                    "prewait delay of ${preWait.inWholeMilliseconds}ms, turn it off using io.ktor.development=true",
                )
                delay(preWait.inWholeMilliseconds)
            }
            engine.environment.log.info("Shutting down HTTP server...")
            engine.stop(grace.inWholeMilliseconds, timeout.inWholeMilliseconds)
            engine.environment.log.info("HTTP server shutdown!")
        },
    )
