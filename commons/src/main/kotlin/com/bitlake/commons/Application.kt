/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.commons

import arrow.continuations.SuspendApp
import arrow.fx.coroutines.continuations.resource
import io.ktor.server.application.Application
import io.ktor.server.netty.Netty
import kotlinx.coroutines.awaitCancellation
import kotlin.time.Duration.Companion.seconds

object ServerPort {
    var port = 0
}

fun runServer(moduleRegistry: Application.() -> Unit): Unit = SuspendApp {
    resource {
        val engine = server(Netty, preWait = 1.seconds).bind()
        ServerPort.port = engine.resolvedConnectors().first().port
        engine.application.moduleRegistry()
    }.use { awaitCancellation() }
}
