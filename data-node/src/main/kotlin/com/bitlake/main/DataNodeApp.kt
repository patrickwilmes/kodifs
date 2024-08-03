/*
* Copyright (c) 2024, Patrick Wilmes <p.wilmes89@gmail.com>
* All rights reserved.
*
* SPDX-License-Identifier: BSD-2-Clause
*/
package com.bitlake.main

import com.bitlake.commons.GlobalKoinContext
import com.bitlake.commons.runServer
import com.bitlake.main.metrics.ConnectionMonitor
import com.bitlake.main.metrics.heartbeatModule
import com.bitlake.main.metrics.setupHeartbeat
import io.ktor.server.application.ApplicationCallPipeline
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin


fun main() {
    runServer {
        val application = this
        install(Koin) {
            modules(
                brokerModule(),
                heartbeatModule,
                module { single { application } } // we need a reference of the ktor app in background jobs
            )
            GlobalKoinContext.koin = koin
        }
        intercept(ApplicationCallPipeline.Monitoring) {
            ConnectionMonitor.incrementConnectionCount()
            try {
                proceed()
            } finally {
                ConnectionMonitor.decrementConnectionCount()
            }
        }
        setupHeartbeat()
    }
}
